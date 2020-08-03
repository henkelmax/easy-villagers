package de.maxhenkel.easyvillagers.gui;

import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FoodSlot extends Slot {

    public FoodSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return VillagerEntity.FOOD_VALUES.getOrDefault(stack.getItem(), 0) > 0;
    }

}
