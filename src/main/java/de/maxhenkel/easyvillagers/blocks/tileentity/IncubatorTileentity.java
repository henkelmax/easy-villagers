package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.tileentity.IServerTickableBlockEntity;
import de.maxhenkel.easyvillagers.inventory.SimpleInventory;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.gui.VillagerIncubateSlot;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible;
import de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


public class IncubatorTileentity extends VillagerTileentity implements IServerTickableBlockEntity, ITRCompatible {

    protected TRSlotConfiguration trSlotConfig;

    @Override
    public Object getSlotConfiguration() {
        return trSlotConfig;
    }


    protected SimpleInventory inputInventory;
    protected SimpleInventory outputInventory;

    @SuppressWarnings("this-escape")
    public IncubatorTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.INCUBATOR, ModBlocks.INCUBATOR.defaultBlockState(), pos, state);
        inputInventory = new SimpleInventory(4, this::setChanged);
        outputInventory = new SimpleInventory(4, this::setChanged);
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
            trSlotConfig = new TRSlotConfiguration();
        }
    }

    @Override
    public void tickServer() {
        if (!hasVillager()) {
            for (int i = 0; i < inputInventory.getContainerSize(); i++) {
                ItemStack stack = inputInventory.getItem(i);
                if (stack.getItem() instanceof VillagerItem) {
                    ItemStack extracted = inputInventory.removeItem(i, 1);
                    setVillager(extracted);
                    sync();
                    break;
                }
            }
        }
        if (hasVillager()) {
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);

            Villager villagerEntity = getVillagerEntity();

            if (villagerEntity.isBaby()) {
                if (advanceAge(Math.min(EasyVillagersMod.CONFIG.server.incubatorSpeed.get(), Math.abs(villagerEntity.getAge())))) {
                    sync();
                }
            } else {
                advanceAge(1);
            }

            if (villagerEntity.getAge() > 20) {
                ItemStack villagerItem = getVillager();
                for (int i = 0; i < outputInventory.getContainerSize(); i++) {
                    if (outputInventory.getItem(i).isEmpty()) {
                        outputInventory.setItem(i, villagerItem.copy());
                        removeVillager();
                        sync();
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        ContainerHelper.saveAllItems(valueOutput.child("InputInventory"), inputInventory.getItems());
        
        ContainerHelper.saveAllItems(valueOutput.child("OutputInventory"), outputInventory.getItems());

        if (trSlotConfig != null) {
            valueOutput.store("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC, trSlotConfig.serialize());
        }
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        valueInput.child("InputInventory").ifPresent(input -> ContainerHelper.loadAllItems(input, inputInventory.getItems()));
        valueInput.child("OutputInventory").ifPresent(input -> ContainerHelper.loadAllItems(input, outputInventory.getItems()));


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

}

