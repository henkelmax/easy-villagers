package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, Main.MODID);

    public static final DeferredHolder<Item, VillagerItem> VILLAGER = ITEM_REGISTER.register("villager", VillagerItem::new);
    public static final DeferredHolder<Item, Item> TRADER = ITEM_REGISTER.register("trader", () -> ModBlocks.TRADER.get().toItem());
    public static final DeferredHolder<Item, Item> AUTO_TRADER = ITEM_REGISTER.register("auto_trader", () -> ModBlocks.AUTO_TRADER.get().toItem());
    public static final DeferredHolder<Item, Item> FARMER = ITEM_REGISTER.register("farmer", () -> ModBlocks.FARMER.get().toItem());
    public static final DeferredHolder<Item, Item> BREEDER = ITEM_REGISTER.register("breeder", () -> ModBlocks.BREEDER.get().toItem());
    public static final DeferredHolder<Item, Item> CONVERTER = ITEM_REGISTER.register("converter", () -> ModBlocks.CONVERTER.get().toItem());
    public static final DeferredHolder<Item, Item> IRON_FARM = ITEM_REGISTER.register("iron_farm", () -> ModBlocks.IRON_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> INCUBATOR = ITEM_REGISTER.register("incubator", () -> ModBlocks.INCUBATOR.get().toItem());

    public static void init() {
        ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
