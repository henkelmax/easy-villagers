package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;

public class TraderRenderer extends TraderRendererBase<TraderTileentity> {

    public TraderRenderer(EntityModelSet entityModelSet, BlockRenderDispatcher blockRenderer) {
        super(entityModelSet, blockRenderer);
    }
}
