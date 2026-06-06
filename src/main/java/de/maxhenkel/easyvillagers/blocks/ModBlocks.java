package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {

    public static final TraderBlock TRADER = registerBlock("trader", TraderBlock::new, BlockBehaviour.Properties.of());
    public static final AutoTraderBlock AUTO_TRADER = registerBlock("auto_trader", AutoTraderBlock::new, BlockBehaviour.Properties.of());
    public static final FarmerBlock FARMER = registerBlock("farmer", FarmerBlock::new, BlockBehaviour.Properties.of());
    public static final BreederBlock BREEDER = registerBlock("breeder", BreederBlock::new, BlockBehaviour.Properties.of());
    public static final ConverterBlock CONVERTER = registerBlock("converter", ConverterBlock::new, BlockBehaviour.Properties.of());
    public static final IronFarmBlock IRON_FARM = registerBlock("iron_farm", IronFarmBlock::new, BlockBehaviour.Properties.of());
    public static final IncubatorBlock INCUBATOR = registerBlock("incubator", IncubatorBlock::new, BlockBehaviour.Properties.of());
    public static final InventoryViewerBlock INVENTORY_VIEWER = registerBlock("inventory_viewer", InventoryViewerBlock::new, BlockBehaviour.Properties.of());

    @SuppressWarnings("unchecked")
    private static <T extends Block> T registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, name));
        T block = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static void init() {
    }

}
