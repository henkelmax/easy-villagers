package de.maxhenkel.easyvillagers.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CombinedContainer implements Container {
    private final Container input;
    private final Container output;

    public CombinedContainer(Container input, Container output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public int getContainerSize() {
        return input.getContainerSize() + output.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return input.isEmpty() && output.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < input.getContainerSize()) {
            return input.getItem(slot);
        }
        return output.getItem(slot - input.getContainerSize());
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot < input.getContainerSize()) {
            return input.removeItem(slot, amount);
        }
        return output.removeItem(slot - input.getContainerSize(), amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot < input.getContainerSize()) {
            return input.removeItemNoUpdate(slot);
        }
        return output.removeItemNoUpdate(slot - input.getContainerSize());
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot < input.getContainerSize()) {
            input.setItem(slot, stack);
        } else {
            output.setItem(slot - input.getContainerSize(), stack);
        }
    }

    @Override
    public void setChanged() {
        input.setChanged();
        output.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return input.stillValid(player) && output.stillValid(player);
    }

    @Override
    public void clearContent() {
        input.clearContent();
        output.clearContent();
    }
}
