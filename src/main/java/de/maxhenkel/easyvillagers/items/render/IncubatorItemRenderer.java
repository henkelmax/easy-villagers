package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.IncubatorRenderer;
import net.minecraft.core.BlockPos;

public class IncubatorItemRenderer extends BlockItemRendererBase<IncubatorRenderer, IncubatorTileentity> {

    public IncubatorItemRenderer() {
        super(IncubatorRenderer::new, () -> new IncubatorTileentity(BlockPos.ZERO, ModBlocks.INCUBATOR.defaultBlockState()));
    }

}
