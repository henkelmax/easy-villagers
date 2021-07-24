package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.ConverterRenderer;
import net.minecraft.core.BlockPos;

public class ConverterItemRenderer extends BlockItemRendererBase<ConverterRenderer, ConverterTileentity> {

    public ConverterItemRenderer() {
        super(ConverterRenderer::new, () -> new ConverterTileentity(BlockPos.ZERO, ModBlocks.CONVERTER.defaultBlockState()));
    }

}
