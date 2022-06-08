package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

    public static final RegistryObject<VillagerItem> VILLAGER = ITEM_REGISTER.register("villager", VillagerItem::new);
    public static final RegistryObject<Item> TRADER = ITEM_REGISTER.register("trader", () -> ModBlocks.TRADER.get().toItem());
    public static final RegistryObject<Item> AUTO_TRADER = ITEM_REGISTER.register("auto_trader", () -> ModBlocks.AUTO_TRADER.get().toItem());
    public static final RegistryObject<Item> FARMER = ITEM_REGISTER.register("farmer", () -> ModBlocks.FARMER.get().toItem());
    public static final RegistryObject<Item> BREEDER = ITEM_REGISTER.register("breeder", () -> ModBlocks.BREEDER.get().toItem());
    public static final RegistryObject<Item> CONVERTER = ITEM_REGISTER.register("converter", () -> ModBlocks.CONVERTER.get().toItem());
    public static final RegistryObject<Item> IRON_FARM = ITEM_REGISTER.register("iron_farm", () -> ModBlocks.IRON_FARM.get().toItem());
    public static final RegistryObject<Item> INCUBATOR = ITEM_REGISTER.register("incubator", () -> ModBlocks.INCUBATOR.get().toItem());

    public static void init() {
        ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
