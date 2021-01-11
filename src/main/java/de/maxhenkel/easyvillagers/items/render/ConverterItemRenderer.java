package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.ConverterRenderer;

public class ConverterItemRenderer extends BlockItemRendererBase<ConverterRenderer, ConverterTileentity> {

    public ConverterItemRenderer() {
        super(ConverterRenderer::new, ConverterTileentity::new);
    }

}
