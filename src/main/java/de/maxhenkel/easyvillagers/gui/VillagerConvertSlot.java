package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class VillagerConvertSlot extends Slot {

    public VillagerConvertSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof VillagerItem) {
            return true;
        } else if (stack.getItem() == Items.GOLDEN_APPLE) {
            return true;
        } else if (ConverterTileentity.isWeakness(stack)) {
            return true;
        } else {
            return false;
        }
    }

}
