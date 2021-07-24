package de.maxhenkel.easyvillagers.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FoodSlot extends Slot {

    public FoodSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return Villager.FOOD_POINTS.getOrDefault(stack.getItem(), 0) > 0;
    }

}
