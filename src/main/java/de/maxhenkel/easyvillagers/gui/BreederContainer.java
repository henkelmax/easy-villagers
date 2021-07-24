package de.maxhenkel.easyvillagers.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class BreederContainer extends InputOutputContainer {

    public BreederContainer(int id, Inventory playerInventory, Container foodInventory, Container outputInventory) {
        super(Containers.BREEDER_CONTAINER, id, playerInventory, foodInventory, outputInventory);
    }

    public BreederContainer(int id, Inventory playerInventory) {
        super(Containers.BREEDER_CONTAINER, id, playerInventory);
    }

    @Override
    public Slot getInputSlot(Container inventory, int id, int x, int y) {
        return new FoodSlot(inventory, id, x, y);
    }

}
