package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderer;
import net.minecraft.core.BlockPos;

public class BreederItemRenderer extends BlockItemRendererBase<BreederRenderer, BreederTileentity> {

    public BreederItemRenderer() {
        super(BreederRenderer::new, () -> new BreederTileentity(BlockPos.ZERO, ModBlocks.BREEDER.defaultBlockState()));
    }

}
