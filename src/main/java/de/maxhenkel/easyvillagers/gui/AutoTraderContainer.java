package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ContainerBase;
import de.maxhenkel.corelib.inventory.LockedSlot;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AutoTraderContainer extends ContainerBase {

    private AutoTraderTileentity trader;

    public AutoTraderContainer(int id, Inventory playerInventory, Container tradeSlots, AutoTraderTileentity trader, Container inputItems, Container outputItems) {
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

    public AutoTraderContainer(int id, Inventory playerInventory, AutoTraderTileentity trader, Container inputItems, Container outputItems) {
        this(id, playerInventory, trader.getTradeGuiInv(), trader, inputItems, outputItems);
    }

    public AutoTraderContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(3), null, new SimpleContainer(4), new SimpleContainer(4));
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
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < getInventorySize()) {
                if (!moveItemStackTo(stack, getInventorySize(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, 3, getInventorySize(), false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

}
