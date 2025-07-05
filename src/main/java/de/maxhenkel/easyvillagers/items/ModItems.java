package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    private static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(EasyVillagersMod.MODID);

    public static final DeferredHolder<Item, VillagerItem> VILLAGER = ITEM_REGISTER.registerItem("villager", VillagerItem::new);
    public static final DeferredHolder<Item, BlockItem> TRADER = ITEM_REGISTER.registerSimpleBlockItem(ModBlocks.TRADER);
    public static final DeferredHolder<Item, BlockItem> AUTO_TRADER = ITEM_REGISTER.registerSimpleBlockItem(ModBlocks.AUTO_TRADER);
    public static final DeferredHolder<Item, BlockItem> FARMER = ITEM_REGISTER.registerSimpleBlockItem(ModBlocks.FARMER);
    public static final DeferredHolder<Item, BlockItem> BREEDER = ITEM_REGISTER.registerSimpleBlockItem(ModBlocks.BREEDER);
    public static final DeferredHolder<Item, BlockItem> CONVERTER = ITEM_REGISTER.registerSimpleBlockItem(ModBlocks.CONVERTER);
    public static final DeferredHolder<Item, BlockItem> IRON_FARM = ITEM_REGISTER.registerSimpleBlockItem(ModBlocks.IRON_FARM);
    public static final DeferredHolder<Item, BlockItem> INCUBATOR = ITEM_REGISTER.registerSimpleBlockItem(ModBlocks.INCUBATOR);
    public static final DeferredHolder<Item, BlockItem> INVENTORY_VIEWER = ITEM_REGISTER.registerSimpleBlockItem(ModBlocks.INVENTORY_VIEWER);

    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, EasyVillagersMod.MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<VillagerData>> VILLAGER_DATA_COMPONENT = DATA_COMPONENT_TYPE_REGISTER.register("villager", () -> DataComponentType.<VillagerData>builder().cacheEncoding().persistent(VillagerData.CODEC).networkSynchronized(VillagerData.STREAM_CODEC).build());

    public static void init(IEventBus eventBus) {
        ITEM_REGISTER.register(eventBus);
        DATA_COMPONENT_TYPE_REGISTER.register(eventBus);
    }

}
