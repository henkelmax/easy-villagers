package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;

public class AutoTraderRenderer extends TraderRendererBase<AutoTraderTileentity> {

    public AutoTraderRenderer(EntityModelSet entityModelSet, BlockRenderDispatcher blockRenderer) {
        super(entityModelSet, blockRenderer);
    }

}
