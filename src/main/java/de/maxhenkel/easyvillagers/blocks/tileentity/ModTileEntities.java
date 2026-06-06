package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModTileEntities {

    public static final BlockEntityType<TraderTileentity> TRADER = registerBlockEntity("trader", net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(TraderTileentity::new, ModBlocks.TRADER).build());
    public static final BlockEntityType<AutoTraderTileentity> AUTO_TRADER = registerBlockEntity("auto_trader", net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(AutoTraderTileentity::new, ModBlocks.AUTO_TRADER).build());
    public static final BlockEntityType<FarmerTileentity> FARMER = registerBlockEntity("farmer", net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(FarmerTileentity::new, ModBlocks.FARMER).build());
    public static final BlockEntityType<BreederTileentity> BREEDER = registerBlockEntity("breeder", net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(BreederTileentity::new, ModBlocks.BREEDER).build());
    public static final BlockEntityType<ConverterTileentity> CONVERTER = registerBlockEntity("converter", net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(ConverterTileentity::new, ModBlocks.CONVERTER).build());
    public static final BlockEntityType<IronFarmTileentity> IRON_FARM = registerBlockEntity("iron_farm", net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(IronFarmTileentity::new, ModBlocks.IRON_FARM).build());
    public static final BlockEntityType<IncubatorTileentity> INCUBATOR = registerBlockEntity("incubator", net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(IncubatorTileentity::new, ModBlocks.INCUBATOR).build());
    public static final BlockEntityType<InventoryViewerTileentity> INVENTORY_VIEWER = registerBlockEntity("inventory_viewer", net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(InventoryViewerTileentity::new, ModBlocks.INVENTORY_VIEWER).build());

    private static <T extends BlockEntityType<?>> T registerBlockEntity(String name, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, name), blockEntityType);
    }

    public static void init() {
    }

}
