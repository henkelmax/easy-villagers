package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ModClientTileEntities {

    public static void clientSetup() {
        if (!EasyVillagersMod.CLIENT_CONFIG.renderBlockContents.get()) {
            return;
        }
        BlockEntityRenderers.register(ModTileEntities.TRADER.get(), c -> new TraderRenderer(c.entityModelSet(), c.blockRenderDispatcher()));
        BlockEntityRenderers.register(ModTileEntities.AUTO_TRADER.get(), c -> new AutoTraderRenderer(c.entityModelSet(), c.blockRenderDispatcher()));
        BlockEntityRenderers.register(ModTileEntities.FARMER.get(), c -> new FarmerRenderer(c.entityModelSet(), c.blockRenderDispatcher()));
        BlockEntityRenderers.register(ModTileEntities.BREEDER.get(), c -> new BreederRenderer(c.entityModelSet(), c.materials()));
        BlockEntityRenderers.register(ModTileEntities.CONVERTER.get(), c -> new ConverterRenderer(c.entityModelSet()));
        BlockEntityRenderers.register(ModTileEntities.IRON_FARM.get(), c -> new IronFarmRenderer(c.entityModelSet()));
        BlockEntityRenderers.register(ModTileEntities.INCUBATOR.get(), c -> new IncubatorRenderer(c.entityModelSet()));
        BlockEntityRenderers.register(ModTileEntities.INVENTORY_VIEWER.get(), c -> new InventoryViewerRenderer(c.entityModelSet()));
    }

}
