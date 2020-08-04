package de.maxhenkel.easyvillagers.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class ConverterContainer extends InputOutputContainer {

    public ConverterContainer(int id, PlayerInventory playerInventory, IInventory inputInventory, IInventory outputInventory) {
        super(Containers.CONVERTER_CONTAINER, id, playerInventory, inputInventory, outputInventory);
    }

    public ConverterContainer(int id, PlayerInventory playerInventory) {
        super(Containers.CONVERTER_CONTAINER, id, playerInventory);
    }

    @Override
    public Slot getInputSlot(IInventory inventory, int id, int x, int y) {
        return new VillagerConvertSlot(inventory, id, x, y);
    }

}
