package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.MultiItemStackHandler;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.gui.VillagerIncubateSlot;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class IncubatorTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    protected NonNullList<ItemStack> inputInventory;
    protected NonNullList<ItemStack> outputInventory;

    protected MultiItemStackHandler itemHandler;

    public IncubatorTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.INCUBATOR.get(), ModBlocks.INCUBATOR.get().defaultBlockState(), pos, state);
        inputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        itemHandler = new MultiItemStackHandler(inputInventory, outputInventory, VillagerIncubateSlot::isValid);
    }

    @Override
    public void tickServer() {
        if (!hasVillager()) {
            for (ItemStack stack : inputInventory) {
                if (stack.getItem() instanceof VillagerItem) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    setVillager(copy);
                    stack.shrink(1);
                    sync();
                    break;
                }
            }
        }
        if (hasVillager()) {
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);

            Villager villagerEntity = getVillagerEntity();

            if (villagerEntity.isBaby()) {
                if (advanceAge(Math.min(Main.SERVER_CONFIG.incubatorSpeed.get(), Math.abs(villagerEntity.getAge())))) {
                    sync();
                }
            } else {
                advanceAge(1);
            }

            if (villagerEntity.getAge() > 20) {
                for (int i = 0; i < outputInventory.size(); i++) {
                    ItemStack stack = outputInventory.get(i);
                    if (stack.isEmpty()) {
                        outputInventory.set(i, removeVillager().copy());
                        sync();
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);

        compound.put("InputInventory", ContainerHelper.saveAllItems(new CompoundTag(), inputInventory, true, provider));
        compound.put("OutputInventory", ContainerHelper.saveAllItems(new CompoundTag(), outputInventory, true, provider));
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        compound.getCompound("InputInventory").ifPresent(t -> VillagerData.convertInventory(t, inputInventory, provider));
        compound.getCompound("OutputInventory").ifPresent(t -> VillagerData.convertInventory(t, outputInventory, provider));
        super.loadAdditional(compound, provider);
    }

    public Container getInputInventory() {
        return new ItemListInventory(inputInventory, this::setChanged);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory, this::setChanged);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

}
