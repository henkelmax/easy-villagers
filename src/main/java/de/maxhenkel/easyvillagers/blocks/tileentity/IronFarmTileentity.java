package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.ITickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.OutputItemHandler;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Collections;
import java.util.List;

public class IronFarmTileentity extends VillagerTileentity implements ITickableBlockEntity {

    private static ResourceKey<LootTable> GOLEM_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("entities/iron_golem"));

    protected NonNullList<ItemStack> inventory;

    protected long timer;

    protected ItemStackHandler itemHandler;
    protected OutputItemHandler outputItemHandler;

    public IronFarmTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.IRON_FARM.get(), ModBlocks.IRON_FARM.get().defaultBlockState(), pos, state);
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        itemHandler = new ItemStackHandler(inventory);
        outputItemHandler = new OutputItemHandler(inventory);
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
        if (!(level instanceof ServerLevel)) {
            return Collections.emptyList();
        }
        ServerLevel serverWorld = (ServerLevel) level;

        LootParams.Builder builder = new LootParams.Builder(serverWorld)
                .withParameter(LootContextParams.THIS_ENTITY, new IronGolem(EntityType.IRON_GOLEM, level))
                .withParameter(LootContextParams.ORIGIN, new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()))
                .withParameter(LootContextParams.DAMAGE_SOURCE, serverWorld.damageSources().lava());

        LootParams lootContext = builder.create(LootContextParamSets.ENTITY);

        LootTable lootTable = serverWorld.getServer().reloadableRegistries().getLootTable(GOLEM_LOOT_TABLE);

        return lootTable.getRandomItems(lootContext);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(inventory, this::setChanged);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        ContainerHelper.saveAllItems(valueOutput, inventory, false);
        valueOutput.putLong("Timer", timer);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        ContainerHelper.loadAllItems(valueInput, inventory);
        timer = valueInput.getLongOr("Timer", 0L);
        super.loadAdditional(valueInput);
    }

    public static int getGolemSpawnTime() {
        return Main.SERVER_CONFIG.golemSpawnTime.get() - 20 * 10;
    }

    public static int getGolemKillTime() {
        return getGolemSpawnTime() + 20 * 10;
    }

    public IItemHandler getItemHandler() {
        return outputItemHandler;
    }

}
