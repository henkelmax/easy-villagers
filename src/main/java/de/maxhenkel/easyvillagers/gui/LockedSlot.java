package de.maxhenkel.easyvillagers.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LockedSlot extends Slot {

    private final boolean canTake;
    private final boolean canPut;

    public LockedSlot(Container inventoryIn, int index, int xPosition, int yPosition, boolean canTake, boolean canPut) {
        super(inventoryIn, index, xPosition, yPosition);
        this.canTake = canTake;
        this.canPut = canPut;
    }

    public LockedSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        this(inventoryIn, index, xPosition, yPosition, false, false);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return canPut;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return canTake;
    }

}
