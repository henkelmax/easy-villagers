package de.maxhenkel.easyvillagers.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class BreederContainer extends InputOutputContainer {

    public BreederContainer(int id, PlayerInventory playerInventory, IInventory foodInventory, IInventory outputInventory) {
        super(Containers.BREEDER_CONTAINER, id, playerInventory, foodInventory, outputInventory);
    }

    public BreederContainer(int id, PlayerInventory playerInventory) {
        super(Containers.BREEDER_CONTAINER, id, playerInventory);
    }

    @Override
    public Slot getInputSlot(IInventory inventory, int id, int x, int y) {
        return new FoodSlot(inventory, id, x, y);
    }

}
