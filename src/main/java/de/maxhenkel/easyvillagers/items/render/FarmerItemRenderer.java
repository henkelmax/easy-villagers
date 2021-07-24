package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.FarmerRenderer;
import net.minecraft.core.BlockPos;

public class FarmerItemRenderer extends BlockItemRendererBase<FarmerRenderer, FarmerTileentity> {

    public FarmerItemRenderer() {
        super(FarmerRenderer::new, () -> new FarmerTileentity(BlockPos.ZERO, ModBlocks.FARMER.defaultBlockState()));
    }

}
