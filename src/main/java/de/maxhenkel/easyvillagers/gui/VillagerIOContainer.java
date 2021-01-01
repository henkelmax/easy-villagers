package de.maxhenkel.easyvillagers.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class VillagerIOContainer extends InputOutputContainer {

    public VillagerIOContainer(int id, PlayerInventory playerInventory, IInventory inputInventory, IInventory outputInventory) {
        super(Containers.VILLAGER_IO_CONTAINER, id, playerInventory, inputInventory, outputInventory);
    }

    public VillagerIOContainer(int id, PlayerInventory playerInventory) {
        super(Containers.VILLAGER_IO_CONTAINER, id, playerInventory);
    }

    @Override
    public Slot getInputSlot(IInventory inventory, int id, int x, int y) {
        return new VillagerConvertSlot(inventory, id, x, y);
    }

}
