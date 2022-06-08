package de.maxhenkel.easyvillagers.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ConverterContainer extends InputOutputContainer {

    public ConverterContainer(int id, Inventory playerInventory, Container inputInventory, Container outputInventory) {
        super(Containers.CONVERTER_CONTAINER.get(), id, playerInventory, inputInventory, outputInventory);
    }

    public ConverterContainer(int id, Inventory playerInventory) {
        super(Containers.CONVERTER_CONTAINER.get(), id, playerInventory);
    }

    @Override
    public Slot getInputSlot(Container inventory, int id, int x, int y) {
        return new VillagerConvertSlot(inventory, id, x, y);
    }

}
