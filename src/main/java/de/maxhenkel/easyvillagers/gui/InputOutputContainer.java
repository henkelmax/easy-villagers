package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ContainerBase;
import de.maxhenkel.corelib.inventory.LockedSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

public abstract class InputOutputContainer extends ContainerBase {

    public InputOutputContainer(ContainerType type, int id, PlayerInventory playerInventory, IInventory inputInventory, IInventory outputInventory) {
        super(type, id, playerInventory, null);

        for (int i = 0; i < 4; i++) {
            addSlot(getInputSlot(inputInventory, i, 52 + i * 18, 20));
        }

        for (int i = 0; i < 4; i++) {
            addSlot(new LockedSlot(outputInventory, i, 52 + i * 18, 51, true, false));
        }

        addPlayerInventorySlots();
    }

    public InputOutputContainer(ContainerType type, int id, PlayerInventory playerInventory) {
        this(type, id, playerInventory, new Inventory(4), new Inventory(4));
    }

    @Override
    public int getInvOffset() {
        return -2;
    }

    @Override
    public int getInventorySize() {
        return 8;
    }

    public abstract Slot getInputSlot(IInventory inventory, int id, int x, int y);

}
