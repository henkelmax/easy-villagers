package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class IncubatorTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    private NonNullList<ItemStack> inputInventory;
    private NonNullList<ItemStack> outputInventory;

    public IncubatorTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.INCUBATOR, ModBlocks.INCUBATOR.defaultBlockState(), pos, state);
        inputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
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
    public CompoundTag save(CompoundTag compound) {
        compound.put("InputInventory", ContainerHelper.saveAllItems(new CompoundTag(), inputInventory, true));
        compound.put("OutputInventory", ContainerHelper.saveAllItems(new CompoundTag(), outputInventory, true));
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        ContainerHelper.loadAllItems(compound.getCompound("InputInventory"), inputInventory);
        ContainerHelper.loadAllItems(compound.getCompound("OutputInventory"), outputInventory);
        super.load(compound);
    }

    public Container getInputInventory() {
        return new ItemListInventory(inputInventory, this::setChanged);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory, this::setChanged);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side != null && side.equals(Direction.DOWN)) {
                return LazyOptional.of(this::getOutputInventoryItemHandler).cast();
            } else {
                return LazyOptional.of(this::getInputInventoryItemHandler).cast();
            }

        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable foodInventoryHandler;

    public IItemHandlerModifiable getInputInventoryItemHandler() {
        if (foodInventoryHandler == null) {
            foodInventoryHandler = new ItemStackHandler(inputInventory);
        }
        return foodInventoryHandler;
    }

    private IItemHandlerModifiable outputInventoryHandler;

    public IItemHandlerModifiable getOutputInventoryItemHandler() {
        if (outputInventoryHandler == null) {
            outputInventoryHandler = new ItemStackHandler(outputInventory);
        }
        return outputInventoryHandler;
    }
}
