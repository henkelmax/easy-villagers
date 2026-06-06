package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

    public static final VillagerItem VILLAGER = registerItem("villager", VillagerItem::new, new Item.Properties());
    public static final BlockItem TRADER = registerItem("trader", p -> new BlockItem(ModBlocks.TRADER, p), new Item.Properties().useBlockDescriptionPrefix());
    public static final BlockItem AUTO_TRADER = registerItem("auto_trader", p -> new BlockItem(ModBlocks.AUTO_TRADER, p), new Item.Properties().useBlockDescriptionPrefix());
    public static final BlockItem FARMER = registerItem("farmer", p -> new BlockItem(ModBlocks.FARMER, p), new Item.Properties().useBlockDescriptionPrefix());
    public static final BlockItem BREEDER = registerItem("breeder", p -> new BlockItem(ModBlocks.BREEDER, p), new Item.Properties().useBlockDescriptionPrefix());
    public static final BlockItem CONVERTER = registerItem("converter", p -> new BlockItem(ModBlocks.CONVERTER, p), new Item.Properties().useBlockDescriptionPrefix());
    public static final BlockItem IRON_FARM = registerItem("iron_farm", p -> new BlockItem(ModBlocks.IRON_FARM, p), new Item.Properties().useBlockDescriptionPrefix());
    public static final BlockItem INCUBATOR = registerItem("incubator", p -> new BlockItem(ModBlocks.INCUBATOR, p), new Item.Properties().useBlockDescriptionPrefix());
    public static final BlockItem INVENTORY_VIEWER = registerItem("inventory_viewer", p -> new BlockItem(ModBlocks.INVENTORY_VIEWER, p), new Item.Properties().useBlockDescriptionPrefix());

    public static final DataComponentType<VillagerData> VILLAGER_DATA_COMPONENT = registerDataComponent("villager", DataComponentType.<VillagerData>builder().cacheEncoding().persistent(VillagerData.CODEC).networkSynchronized(VillagerData.STREAM_CODEC).build());

    private static <T extends Item> T registerItem(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, name));
        T item = factory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static <T> DataComponentType<T> registerDataComponent(String name, DataComponentType<T> type) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, name), type);
    }

    public static void init() {
    }

}
