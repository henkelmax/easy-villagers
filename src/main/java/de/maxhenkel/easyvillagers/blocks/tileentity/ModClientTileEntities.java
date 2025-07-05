package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ModClientTileEntities {

    public static void clientSetup() {
        if (!Main.CLIENT_CONFIG.renderBlockContents.get()) {
            return;
        }
        BlockEntityRenderers.register(ModTileEntities.TRADER.get(), c -> new TraderRenderer(c.getModelSet()));
        BlockEntityRenderers.register(ModTileEntities.AUTO_TRADER.get(), c -> new AutoTraderRenderer(c.getModelSet()));
        BlockEntityRenderers.register(ModTileEntities.FARMER.get(), c -> new FarmerRenderer(c.getModelSet()));
        BlockEntityRenderers.register(ModTileEntities.BREEDER.get(), c -> new BreederRenderer(c.getModelSet()));
        BlockEntityRenderers.register(ModTileEntities.CONVERTER.get(), c -> new ConverterRenderer(c.getModelSet()));
        BlockEntityRenderers.register(ModTileEntities.IRON_FARM.get(), c -> new IronFarmRenderer(c.getModelSet()));
        BlockEntityRenderers.register(ModTileEntities.INCUBATOR.get(), c -> new IncubatorRenderer(c.getModelSet()));
        BlockEntityRenderers.register(ModTileEntities.INVENTORY_VIEWER.get(), c -> new InventoryViewerRenderer(c.getModelSet()));
    }

}
