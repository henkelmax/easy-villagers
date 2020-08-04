package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ContainerBase;
import de.maxhenkel.corelib.inventory.LockedSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;

public class OutputContainer extends ContainerBase {

    public OutputContainer(int id, PlayerInventory playerInventory, IInventory outputInventory) {
        super(Containers.OUTPUT_CONTAINER, id, playerInventory, outputInventory);

        for (int i = 0; i < 4; i++) {
            addSlot(new LockedSlot(outputInventory, i, 52 + i * 18, 20, true, false));
        }

        addPlayerInventorySlots();
    }

    public OutputContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(4));
    }

    @Override
    public int getInvOffset() {
        return -33;
    }

    @Override
    public int getInventorySize() {
        return 4;
    }

}
