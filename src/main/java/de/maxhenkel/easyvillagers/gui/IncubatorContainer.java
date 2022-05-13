package de.maxhenkel.easyvillagers.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class IncubatorContainer extends InputOutputContainer {

    public IncubatorContainer(int id, Inventory playerInventory, Container inputInventory, Container outputInventory) {
        super(Containers.INCUBATOR_CONTAINER, id, playerInventory, inputInventory, outputInventory);
    }

    public IncubatorContainer(int id, Inventory playerInventory) {
        super(Containers.INCUBATOR_CONTAINER, id, playerInventory);
    }

    @Override
    public Slot getInputSlot(Container inventory, int id, int x, int y) {
        return new VillagerIncubateSlot(inventory, id, x, y);
    }

}
