package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.tileentity.IServerTickableBlockEntity;
import de.maxhenkel.easyvillagers.inventory.SimpleInventory;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible;
import de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class IronFarmTileentity extends VillagerTileentity implements IServerTickableBlockEntity, ITRCompatible {

    protected TRSlotConfiguration trSlotConfig;

    @Override
    public Object getSlotConfiguration() {
        return trSlotConfig;
    }


    private static final ResourceKey<LootTable> GOLEM_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/iron_golem"));

    protected SimpleInventory inventory;

    protected long timer;

    @SuppressWarnings("this-escape")
    public IronFarmTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.IRON_FARM, ModBlocks.IRON_FARM.defaultBlockState(), pos, state);
        inventory = new SimpleInventory(4, this::setChanged);
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
            trSlotConfig = new TRSlotConfiguration();
        }
    }

    public long getTimer() {
        return timer;
    }

    @Override
    public void tickServer() {
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
                for (ItemStack stack : getDrops()) {
                    for (int i = 0; i < inventory.getContainerSize(); i++) {
                        if (stack.isEmpty()) {
                            break;
                        }
                        ItemStack invStack = inventory.getItem(i);
                        if (invStack.isEmpty()) {
                            inventory.setItem(i, stack.copy());
                            stack.setCount(0);
                        } else if (ItemStack.isSameItemSameComponents(invStack, stack) && invStack.getCount() < invStack.getMaxStackSize()) {
                            int amount = Math.min(stack.getCount(), invStack.getMaxStackSize() - invStack.getCount());
                            invStack.grow(amount);
                            stack.shrink(amount);
                        }
                    }
                }

                timer = 0L;
                sync();
            }
        }
    }

    private List<ItemStack> getDrops() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return Collections.emptyList();
        }

        LootParams.Builder builder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, new IronGolem(EntityType.IRON_GOLEM, level))
                .withParameter(LootContextParams.ORIGIN, new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()))
                .withParameter(LootContextParams.DAMAGE_SOURCE, serverLevel.damageSources().lava());

        LootParams lootContext = builder.create(LootContextParamSets.ENTITY);

        LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(GOLEM_LOOT_TABLE);

        return lootTable.getRandomItems(lootContext);
    }

    public Container getOutputInventory() {
        return inventory;
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        ContainerHelper.saveAllItems(valueOutput, inventory.getItems());
        valueOutput.putLong("Timer", timer);

        if (trSlotConfig != null) {
            valueOutput.store("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC, trSlotConfig.serialize());
        }
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        ContainerHelper.loadAllItems(valueInput, inventory.getItems());
        timer = valueInput.getLongOr("Timer", 0L);

        if (trSlotConfig != null && valueInput.contains("TRSlotConfig")) {
            valueInput.read("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC).ifPresent(tag -> trSlotConfig.deserialize(tag));
        }

        super.loadAdditional(valueInput);
    }

    public static int getGolemSpawnTime() {
        return EasyVillagersMod.CONFIG.server.golemSpawnTime.get() - 20 * 10;
    }

    public static int getGolemKillTime() {
        return getGolemSpawnTime() + 20 * 10;
    }

}

