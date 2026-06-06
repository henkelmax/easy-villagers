package de.maxhenkel.easyvillagers.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class ContainerBase extends AbstractContainerMenu {

    protected Container playerInventory;
    protected Container inventory;

    public ContainerBase(MenuType<?> type, int id, Container playerInventory, Container inventory) {
        super(type, id);
        this.playerInventory = playerInventory;
        this.inventory = inventory;
    }

    protected void addPlayerInventorySlots() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + getInvOffset()));
            }
        }
        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, 142 + getInvOffset()));
        }
    }

    public int getInvOffset() {
        return 0;
    }

    public int getInventorySize() {
        return inventory != null ? inventory.getContainerSize() : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory != null ? inventory.stillValid(player) : true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int inventorySize = getInventorySize();
            if (index < inventorySize) {
                if (!moveItemStackTo(itemstack1, inventorySize, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(itemstack1, 0, inventorySize, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }
}
