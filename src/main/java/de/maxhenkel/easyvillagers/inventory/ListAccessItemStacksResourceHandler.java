package de.maxhenkel.easyvillagers.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class ListAccessItemStacksResourceHandler extends ItemStacksResourceHandler {

    public ListAccessItemStacksResourceHandler(int size) {
        super(size);
    }

    public ListAccessItemStacksResourceHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public NonNullList<ItemStack> getRaw() {
        return stacks;
    }

    public void setRaw(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }
}
