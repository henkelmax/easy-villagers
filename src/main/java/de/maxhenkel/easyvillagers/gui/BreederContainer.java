package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ContainerBase;
import de.maxhenkel.corelib.inventory.LockedSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;

public class BreederContainer extends ContainerBase {

    public BreederContainer(int id, PlayerInventory playerInventory, IInventory foodInventory, IInventory outputInventory) {
        super(Containers.BREEDER_CONTAINER, id, playerInventory, null);

        for (int i = 0; i < 4; i++) {
            addSlot(new FoodSlot(foodInventory, i, 52 + i * 18, 20));
        }

        for (int i = 0; i < 4; i++) {
            addSlot(new LockedSlot(outputInventory, i, 52 + i * 18, 51, true, false));
        }

        addPlayerInventorySlots();
    }

    public BreederContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(4), new Inventory(4));
    }

    @Override
    public int getInvOffset() {
        return -2;
    }

    @Override
    public int getInventorySize() {
        return 8;
    }

}
