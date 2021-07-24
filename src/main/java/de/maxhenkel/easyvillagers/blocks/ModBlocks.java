package de.maxhenkel.easyvillagers.blocks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ModBlocks {

    public static final TraderBlock TRADER = new TraderBlock();
    public static final AutoTraderBlock AUTO_TRADER = new AutoTraderBlock();
    public static final FarmerBlock FARMER = new FarmerBlock();
    public static final BreederBlock BREEDER = new BreederBlock();
    public static final ConverterBlock CONVERTER = new ConverterBlock();
    public static final IronFarmBlock IRON_FARM = new IronFarmBlock();
    public static final IncubatorBlock INCUBATOR = new IncubatorBlock();

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                TRADER,
                AUTO_TRADER,
                FARMER,
                BREEDER,
                CONVERTER,
                IRON_FARM,
                INCUBATOR
        );

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ItemBlockRenderTypes.setRenderLayer(TRADER, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(AUTO_TRADER, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(FARMER, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BREEDER, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CONVERTER, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(IRON_FARM, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(INCUBATOR, RenderType.cutout());
        }
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                TRADER.toItem(),
                AUTO_TRADER.toItem(),
                FARMER.toItem(),
                BREEDER.toItem(),
                CONVERTER.toItem(),
                IRON_FARM.toItem(),
                INCUBATOR.toItem()
        );
    }

}
