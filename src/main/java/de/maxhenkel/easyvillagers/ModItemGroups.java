package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ModItemGroups {

    public static final ItemGroup TAB_EASY_VILLAGERS = new ItemGroup("easy_villagers") {

        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.VILLAGER);
        }

        @Override
        public void fill(NonNullList<ItemStack> list) {
            super.fill(list);
            list.add(new ItemStack(ModItems.VILLAGER));
            list.add(VillagerItem.getBabyVillager());
        }

    };

}
