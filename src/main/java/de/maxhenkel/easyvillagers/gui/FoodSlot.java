package de.maxhenkel.easyvillagers.gui;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.food.VillagerFood;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class FoodSlot extends Slot {

    public FoodSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return isValid(ItemResource.of(stack));
    }

    public static boolean isValid(ItemResource resource) {
        VillagerFood villagerFood = resource.get(DataComponents.VILLAGER_FOOD);
        return villagerFood != null && villagerFood.nutrition() > 0;
    }

}
