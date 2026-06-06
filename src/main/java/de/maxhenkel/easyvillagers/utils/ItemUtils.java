package de.maxhenkel.easyvillagers.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {

    public static void decrItemStack(ItemStack stack, Player player) {
        stack.shrink(1);
        if (stack.isEmpty()) {
            player.getInventory().removeItem(stack);
        }
    }

}
