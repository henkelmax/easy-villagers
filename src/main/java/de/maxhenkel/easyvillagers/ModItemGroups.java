package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

    public static final CreativeModeTab TAB_EASY_VILLAGERS = new CreativeModeTab("easy_villagers") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.VILLAGER);
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> list) {
            super.fillItemList(list);
            list.add(new ItemStack(ModItems.VILLAGER));
            list.add(VillagerItem.getBabyVillager());
        }

    };

}
