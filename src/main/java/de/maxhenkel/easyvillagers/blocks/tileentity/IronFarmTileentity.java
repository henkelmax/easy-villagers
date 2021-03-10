package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collections;
import java.util.List;

public class IronFarmTileentity extends VillagerTileentity implements ITickableTileEntity {

    private static final ResourceLocation GOLEM_LOOT_TABLE = new ResourceLocation("entities/iron_golem");

    private NonNullList<ItemStack> inventory;

    private long timer;

    public IronFarmTileentity() {
        super(ModTileEntities.IRON_FARM, ModBlocks.IRON_FARM.defaultBlockState());
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    public long getTimer() {
        return timer;
    }

    @Override
    public void tick() {
        EasyVillagerEntity v = getVillagerEntity();
        if (v != null) {
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.ZOMBIE_AMBIENT);

            if (advanceAge()) {
                sync();
            }

            timer++;
            setChanged();

            if (timer == getGolemSpawnTime()) {
                VillagerBlockBase.playVillagerSound(level, getBlockPos(), SoundEvents.ZOMBIE_AMBIENT);
                sync();
            } else if (timer > getGolemSpawnTime() && timer < getGolemKillTime()) {
                if (timer % 20L == 0L) {
                    VillagerBlockBase.playVillagerSound(level, getBlockPos(), SoundEvents.IRON_GOLEM_HURT);
                }
            } else if (timer >= getGolemKillTime()) {
                VillagerBlockBase.playVillagerSound(level, getBlockPos(), SoundEvents.IRON_GOLEM_DEATH);
                IItemHandlerModifiable itemHandler = getItemHandler();
                for (ItemStack drop : getDrops()) {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        drop = itemHandler.insertItem(i, drop, false);
                        if (drop.isEmpty()) {
                            break;
                        }
                    }
                }

                timer = 0L;
                sync();
            }
        }
    }

    private List<ItemStack> getDrops() {
        if (!(level instanceof ServerWorld)) {
            return Collections.emptyList();
        }
        ServerWorld serverWorld = (ServerWorld) level;

        LootContext.Builder builder = new LootContext.Builder(serverWorld)
                .withRandom(serverWorld.random)
                .withParameter(LootParameters.THIS_ENTITY, new IronGolemEntity(EntityType.IRON_GOLEM, level))
                .withParameter(LootParameters.ORIGIN, new Vector3d(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()))
                .withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.LAVA);

        LootContext lootContext = builder.create(LootParameterSets.ENTITY);

        LootTable lootTable = serverWorld.getServer().getLootTables().get(GOLEM_LOOT_TABLE);

        return lootTable.getRandomItems(lootContext);
    }

    public IInventory getOutputInventory() {
        return new ItemListInventory(inventory, this::setChanged);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, inventory, false);
        compound.putLong("Timer", timer);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        ItemStackHelper.loadAllItems(compound, inventory);
        timer = compound.getLong("Timer");
        super.load(state, compound);
    }

    private IItemHandlerModifiable handler;

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(this::getItemHandler).cast();
        }
        return super.getCapability(cap, side);
    }

    public IItemHandlerModifiable getItemHandler() {
        if (handler == null) {
            handler = new ItemStackHandler(inventory);
        }
        return handler;
    }

    public static int getGolemSpawnTime() {
        return Main.SERVER_CONFIG.golemSpawnTime.get() - 20 * 10;
    }

    public static int getGolemKillTime() {
        return getGolemSpawnTime() + 20 * 10;
    }

}
