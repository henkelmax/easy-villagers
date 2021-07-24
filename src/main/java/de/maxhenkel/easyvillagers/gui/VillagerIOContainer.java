package de.maxhenkel.easyvillagers.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class VillagerIOContainer extends InputOutputContainer {

    public VillagerIOContainer(int id, Inventory playerInventory, Container inputInventory, Container outputInventory) {
        super(Containers.VILLAGER_IO_CONTAINER, id, playerInventory, inputInventory, outputInventory);
    }

    public VillagerIOContainer(int id, Inventory playerInventory) {
        super(Containers.VILLAGER_IO_CONTAINER, id, playerInventory);
    }

    @Override
    public Slot getInputSlot(Container inventory, int id, int x, int y) {
        return new VillagerConvertSlot(inventory, id, x, y);
    }

}
