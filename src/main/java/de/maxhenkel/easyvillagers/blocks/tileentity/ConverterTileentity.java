package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.tileentity.IServerTickableBlockEntity;
import de.maxhenkel.easyvillagers.inventory.SimpleInventory;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.VillagerConvertSlot;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.state.BlockState;
import de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible;
import de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


import java.util.UUID;

public class ConverterTileentity extends VillagerTileentity implements IServerTickableBlockEntity, ITRCompatible {

    protected TRSlotConfiguration trSlotConfig;

    @Override
    public Object getSlotConfiguration() {
        return trSlotConfig;
    }


    protected SimpleInventory inputInventory;
    protected SimpleInventory outputInventory;

    protected long timer;
    protected UUID owner;

    @SuppressWarnings("this-escape")
    public ConverterTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.CONVERTER, ModBlocks.CONVERTER.defaultBlockState(), pos, state);
        inputInventory = new SimpleInventory(4, this::setChanged);
        outputInventory = new SimpleInventory(4, this::setChanged);
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
            trSlotConfig = new TRSlotConfiguration();
        }
    }

    @Override
    public void tickServer() {
        if (timer <= 0L && !hasVillager()) {
            if (consumeConvertItems()) {
                for (int i = 0; i < inputInventory.getContainerSize(); i++) {
                    ItemStack stack = inputInventory.getItem(i);
                    if (!(stack.getItem() instanceof VillagerItem)) {
                        continue;
                    }
                    ItemStack copy = inputInventory.removeItem(i, 1);
                    setVillager(copy);
                    sync();
                    break;
                }
            }
        }
        if (hasVillager()) {
            if (advanceAge()) {
                sync();
            }
            if (timer == getZombifyTime()) {
                VillagerBlockBase.playVillagerSound(level, worldPosition, SoundEvents.ZOMBIE_INFECT);
                sync();
            } else if (timer == getCureTime()) {
                VillagerBlockBase.playVillagerSound(level, worldPosition, SoundEvents.ZOMBIE_VILLAGER_CURE);
                sync();
            } else if (timer == getConvertTime()) {
                VillagerBlockBase.playVillagerSound(level, worldPosition, SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
                sync();
            } else if (timer >= getFinalizeTime()) {
                Player ownerPlayer = getOwnerPlayer();
                if (ownerPlayer != null) {
                    for (int i = 0; i < outputInventory.getContainerSize(); i++) {
                        ItemStack stack = outputInventory.getItem(i);
                        if (stack.isEmpty()) {
                            EasyVillagerEntity villagerEntity = getVillagerEntity();
                            villagerEntity.onReputationEventFrom(ReputationEventType.ZOMBIE_VILLAGER_CURED, ownerPlayer);
                            outputInventory.setItem(i, removeVillager().copy());
                            timer = 0L;
                            sync();
                            break;
                        }
                    }
                }
            }

            timer++;
            setChanged();
            if (timer < getZombifyTime() || timer >= getConvertTime()) {
                VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);
            } else {
                VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.ZOMBIE_VILLAGER_AMBIENT);
            }
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.ZOMBIE_AMBIENT);
        } else if (timer != 0L) {
            timer = 0L;
            setChanged();
        }
    }

    private boolean consumeConvertItems() {
        int appleSlot = -1;
        int potionSlot = -1;
        boolean hasVillagerItem = false;
        for (int i = 0; i < inputInventory.getContainerSize(); i++) {
            ItemStack stack = inputInventory.getItem(i);
            if (stack.getItem() == Items.GOLDEN_APPLE) {
                appleSlot = i;
            }
            if (isWeakness(stack)) {
                potionSlot = i;
            }
            if (stack.getItem() instanceof VillagerItem) {
                hasVillagerItem = true;
            }
        }

        if (appleSlot == -1 || potionSlot == -1 || !hasVillagerItem) {
            return false;
        }
        
        inputInventory.removeItem(appleSlot, 1);
        inputInventory.removeItem(potionSlot, 1);
        return true;
    }

    public static boolean isWeakness(ItemStack stack) {
        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
        if (potionContents == null) {
            return false;
        }
        return potionContents.potion().filter(potionHolder -> potionHolder.equals(Potions.WEAKNESS) || potionHolder.equals(Potions.LONG_WEAKNESS)).isPresent();
    }

    public long getTimer() {
        return timer;
    }

    public UUID getOwner() {
        return owner;
    }

    public Player getOwnerPlayer() {
        if (owner == null) {
            return null;
        }
        if (level instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) level;
            return serverWorld.getServer().getPlayerList().getPlayer(owner);
        } else {
            return level.getPlayerByUUID(owner);
        }
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        ContainerHelper.saveAllItems(valueOutput.child("InputInventory"), inputInventory.getItems());
        
        ContainerHelper.saveAllItems(valueOutput.child("OutputInventory"), outputInventory.getItems());

        valueOutput.putLong("Timer", timer);
        if (owner != null) {
            valueOutput.storeNullable("Owner", net.minecraft.core.UUIDUtil.CODEC, owner);
    
        if (trSlotConfig != null) {
            valueOutput.store("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC, trSlotConfig.serialize());
        }
    }
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        valueInput.child("InputInventory").ifPresent(input -> ContainerHelper.loadAllItems(input, inputInventory.getItems()));
        valueInput.child("OutputInventory").ifPresent(input -> ContainerHelper.loadAllItems(input, outputInventory.getItems()));

        timer = valueInput.getLongOr("Timer", 0L);
        owner = valueInput.read("Owner", net.minecraft.core.UUIDUtil.CODEC).orElse(null);

        if (trSlotConfig != null && valueInput.contains("TRSlotConfig")) {
            valueInput.read("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC).ifPresent(tag -> trSlotConfig.deserialize(tag));
        }

        super.loadAdditional(valueInput);
    }

    public Container getInputInventory() {
        return inputInventory;
    }

    public Container getOutputInventory() {
        return outputInventory;
    }

    public static int getZombifyTime() {
        return 20 * 3;
    }

    public static int getCureTime() {
        return getZombifyTime() + 20 * 3;
    }

    public static int getConvertTime() {
        return getCureTime() + EasyVillagersMod.CONFIG.server.convertingTime.get();
    }

    public static int getFinalizeTime() {
        return getConvertTime() + 20 * 3;
    }

}

