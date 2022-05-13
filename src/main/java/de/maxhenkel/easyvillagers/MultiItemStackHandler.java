package de.maxhenkel.easyvillagers;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MultiItemStackHandler implements IItemHandler {

    protected NonNullList<ItemStack> modifiable;
    protected NonNullList<ItemStack> takeOnly;
    @Nullable
    protected Predicate<ItemStack> inputValidator;

    public MultiItemStackHandler(NonNullList<ItemStack> modifiable, NonNullList<ItemStack> takeOnly, Predicate<ItemStack> inputValidator) {
        this.modifiable = modifiable;
        this.takeOnly = takeOnly;
        this.inputValidator = inputValidator;
    }

    public MultiItemStackHandler(NonNullList<ItemStack> modifiable, NonNullList<ItemStack> takeOnly) {
        this(modifiable, takeOnly, null);
    }

    @Override
    public int getSlots() {
        return modifiable.size() + takeOnly.size();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return getList(slot).get(getListIndex(slot));
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (!isItemValid(slot, stack)) {
            return stack;
        }

        validateSlotIndex(slot);

        ItemStack existing = getStackInSlot(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                return stack;
            }

            limit -= existing.getCount();
        }

        if (limit <= 0) {
            return stack;
        }

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                getList(slot).set(getListIndex(slot), reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        }

        validateSlotIndex(slot);

        if (!canExtract(slot)) {
            return ItemStack.EMPTY;
        }

        ItemStack existing = getStackInSlot(slot);

        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                getList(slot).set(getListIndex(slot), ItemStack.EMPTY);
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                getList(slot).set(getListIndex(slot), ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot < modifiable.size()) {
            if (inputValidator != null) {
                return inputValidator.test(stack);
            }
            return true;
        }
        return false;
    }

    protected boolean canExtract(int slot) {
        return slot >= modifiable.size();
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= getSlots()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
        }
    }

    protected NonNullList<ItemStack> getList(int slot) {
        validateSlotIndex(slot);
        if (slot < modifiable.size()) {
            return modifiable;
        }
        return takeOnly;
    }

    protected int getListIndex(int slot) {
        validateSlotIndex(slot);
        if (slot < modifiable.size()) {
            return slot;
        }
        return slot - modifiable.size();
    }

}
