package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CreativeTabEvents {

    public static CreativeModeTab TAB_EASY_VILLAGERS;

    @SubscribeEvent
    public static void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
        TAB_EASY_VILLAGERS = event.registerCreativeModeTab(new ResourceLocation(Main.MODID, "easy_villagers"), builder -> {
            builder
                    .icon(() -> new ItemStack(ModItems.VILLAGER.get()))
                    .displayItems((features, output) -> {
                        output.accept(new ItemStack(ModItems.VILLAGER.get()));
                        output.accept(VillagerItem.getBabyVillager());

                        output.accept(new ItemStack(ModBlocks.TRADER.get()));
                        output.accept(new ItemStack(ModBlocks.AUTO_TRADER.get()));
                        output.accept(new ItemStack(ModBlocks.FARMER.get()));
                        output.accept(new ItemStack(ModBlocks.BREEDER.get()));
                        output.accept(new ItemStack(ModBlocks.CONVERTER.get()));
                        output.accept(new ItemStack(ModBlocks.IRON_FARM.get()));
                        output.accept(new ItemStack(ModBlocks.INCUBATOR.get()));
                    })
                    .title(Component.translatable("itemGroup.easy_villagers"))
                    .build();
        });
    }

}
