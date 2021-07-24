package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ContainerBase;
import de.maxhenkel.corelib.inventory.LockedSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public class OutputContainer extends ContainerBase {

    public OutputContainer(int id, Inventory playerInventory, Container outputInventory) {
        super(Containers.OUTPUT_CONTAINER, id, playerInventory, outputInventory);

        for (int i = 0; i < 4; i++) {
            addSlot(new LockedSlot(outputInventory, i, 52 + i * 18, 20, true, false));
        }

        addPlayerInventorySlots();
    }

    public OutputContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(4));
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
