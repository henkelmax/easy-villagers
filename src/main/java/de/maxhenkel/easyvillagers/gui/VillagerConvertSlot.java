package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class VillagerConvertSlot extends Slot {

    public VillagerConvertSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return isValid(ItemResource.of(stack));
    }

    public static boolean isValid(ItemResource stack) {
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
