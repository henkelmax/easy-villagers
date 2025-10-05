package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class VillagerIncubateSlot extends Slot {

    public VillagerIncubateSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return isValid(ItemResource.of(stack));
    }

    public static boolean isValid(ItemResource stack) {
        return stack.getItem() instanceof VillagerItem;
    }

}
