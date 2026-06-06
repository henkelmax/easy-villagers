package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ModClientTileEntities {

    public static void clientSetup() {
        if (!EasyVillagersMod.CONFIG.client.renderBlockContents.get()) {
            return;
        }
        BlockEntityRenderers.register(ModTileEntities.TRADER, c -> new TraderRenderer(c.entityModelSet(), c.blockModelResolver()));
        BlockEntityRenderers.register(ModTileEntities.AUTO_TRADER, c -> new AutoTraderRenderer(c.entityModelSet(), c.blockModelResolver()));
        BlockEntityRenderers.register(ModTileEntities.FARMER, c -> new FarmerRenderer(c.entityModelSet(), c.blockModelResolver()));
        BlockEntityRenderers.register(ModTileEntities.BREEDER, c -> new BreederRenderer(c.entityModelSet(), c.sprites()));
        BlockEntityRenderers.register(ModTileEntities.CONVERTER, c -> new ConverterRenderer(c.entityModelSet()));
        BlockEntityRenderers.register(ModTileEntities.IRON_FARM, c -> new IronFarmRenderer(c.entityModelSet()));
        BlockEntityRenderers.register(ModTileEntities.INCUBATOR, c -> new IncubatorRenderer(c.entityModelSet()));
        BlockEntityRenderers.register(ModTileEntities.INVENTORY_VIEWER, c -> new InventoryViewerRenderer(c.entityModelSet()));
    }

}
