package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderer;
import net.minecraft.core.BlockPos;

public class TraderItemRenderer extends BlockItemRendererBase<TraderRenderer, TraderTileentity> {

    public TraderItemRenderer() {
        super(TraderRenderer::new, () -> new TraderTileentity(BlockPos.ZERO, ModBlocks.TRADER.defaultBlockState()));
    }

}
