package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTileEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Main.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TraderTileentity>> TRADER = BLOCK_ENTITY_REGISTER.register("trader", () ->
            new BlockEntityType<>(TraderTileentity::new, ModBlocks.TRADER.get())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoTraderTileentity>> AUTO_TRADER = BLOCK_ENTITY_REGISTER.register("auto_trader", () ->
            new BlockEntityType<>(AutoTraderTileentity::new, ModBlocks.AUTO_TRADER.get())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FarmerTileentity>> FARMER = BLOCK_ENTITY_REGISTER.register("farmer", () ->
            new BlockEntityType<>(FarmerTileentity::new, ModBlocks.FARMER.get())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BreederTileentity>> BREEDER = BLOCK_ENTITY_REGISTER.register("breeder", () ->
            new BlockEntityType<>(BreederTileentity::new, ModBlocks.BREEDER.get())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ConverterTileentity>> CONVERTER = BLOCK_ENTITY_REGISTER.register("converter", () ->
            new BlockEntityType<>(ConverterTileentity::new, ModBlocks.CONVERTER.get())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IronFarmTileentity>> IRON_FARM = BLOCK_ENTITY_REGISTER.register("iron_farm", () ->
            new BlockEntityType<>(IronFarmTileentity::new, ModBlocks.IRON_FARM.get())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IncubatorTileentity>> INCUBATOR = BLOCK_ENTITY_REGISTER.register("incubator", () ->
            new BlockEntityType<>(IncubatorTileentity::new, ModBlocks.INCUBATOR.get())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InventoryViewerTileentity>> INVENTORY_VIEWER = BLOCK_ENTITY_REGISTER.register("inventory_viewer", () ->
            new BlockEntityType<>(InventoryViewerTileentity::new, ModBlocks.INVENTORY_VIEWER.get())
    );

    public static void init(IEventBus eventBus) {
        BLOCK_ENTITY_REGISTER.register(eventBus);
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BREEDER.get(), (object, context) -> object.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AUTO_TRADER.get(), (object, context) -> object.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FARMER.get(), (object, context) -> object.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CONVERTER.get(), (object, context) -> object.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRON_FARM.get(), (object, context) -> object.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INCUBATOR.get(), (object, context) -> object.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INVENTORY_VIEWER.get(), (object, context) -> object.getItemHandler());
    }

}
