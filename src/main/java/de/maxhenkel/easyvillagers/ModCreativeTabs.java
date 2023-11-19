package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_EASY_VILLAGERS = TAB_REGISTER.register("easy_villagers", () -> {
        return CreativeModeTab.builder()
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

    public static void init() {
        TAB_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
