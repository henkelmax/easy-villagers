package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ContainerBase;
import de.maxhenkel.corelib.inventory.LockedSlot;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class AutoTraderContainer extends ContainerBase {

    private AutoTraderTileentity trader;

    public AutoTraderContainer(int id, PlayerInventory playerInventory, IInventory tradeSlots, AutoTraderTileentity trader, IInventory inputItems, IInventory outputItems) {
        super(Containers.AUTO_TRADER_CONTAINER, id, playerInventory, null);
        this.trader = trader;
        addSlot(new LockedSlot(tradeSlots, 0, 36, 21, true, true));
        addSlot(new LockedSlot(tradeSlots, 1, 62, 21, true, true));
        addSlot(new LockedSlot(tradeSlots, 2, 120, 22, true, true));

        for (int i = 0; i < 4; i++) {
            addSlot(new Slot(inputItems, i, 53 + i * 18, 57));
        }

        for (int i = 0; i < 4; i++) {
            addSlot(new LockedSlot(outputItems, i, 53 + i * 18, 88, true, false));
        }

        addPlayerInventorySlots();
    }

    public AutoTraderContainer(int id, PlayerInventory playerInventory, AutoTraderTileentity trader, IInventory inputItems, IInventory outputItems) {
        this(id, playerInventory, trader.getTradeGuiInv(), trader, inputItems, outputItems);
    }

    public AutoTraderContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(3), null, new Inventory(4), new Inventory(4));
    }

    @Override
    public int getInvOffset() {
        return 36;
    }

    @Override
    public int getInventorySize() {
        return 11;
    }

    public AutoTraderTileentity getTrader() {
        return trader;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index < getInventorySize()) {
                if (!mergeItemStack(stack, getInventorySize(), inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(stack, 3, getInventorySize(), false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
