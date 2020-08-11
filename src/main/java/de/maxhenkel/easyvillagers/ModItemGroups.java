package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
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

            ItemStack babyVillager = new ItemStack(ModItems.VILLAGER);
            VillagerEntity villager = new VillagerEntity(EntityType.VILLAGER, Minecraft.getInstance().world) {
                @Override
                public int getGrowingAge() {
                    return -24000;
                }
            };
            ModItems.VILLAGER.setVillager(babyVillager, villager);
            list.add(babyVillager);
        }

    };

}
