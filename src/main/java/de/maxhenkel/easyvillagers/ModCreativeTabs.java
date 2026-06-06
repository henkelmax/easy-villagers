package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.items.VillagerItem;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static final CreativeModeTab TAB_EASY_VILLAGERS = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "easy_villagers"),
            net.minecraft.world.item.CreativeModeTab.builder(net.minecraft.world.item.CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.easy_villagers"))
                    .icon(() -> new ItemStack(ModItems.VILLAGER))
                    .displayItems((features, output) -> {
                        output.accept(new ItemStack(ModItems.VILLAGER));
                        output.accept(VillagerItem.createBabyVillager());

                        output.accept(new ItemStack(ModBlocks.TRADER));
                        output.accept(new ItemStack(ModBlocks.AUTO_TRADER));
                        output.accept(new ItemStack(ModBlocks.FARMER));
                        output.accept(new ItemStack(ModBlocks.BREEDER));
                        output.accept(new ItemStack(ModBlocks.CONVERTER));
                        output.accept(new ItemStack(ModBlocks.IRON_FARM));
                        output.accept(new ItemStack(ModBlocks.INCUBATOR));
                        output.accept(new ItemStack(ModBlocks.INVENTORY_VIEWER));
                    })
                    .build()
    );

    public static void init() {
    }

}
