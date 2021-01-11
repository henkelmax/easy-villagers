package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderer;

public class BreederItemRenderer extends BlockItemRendererBase<BreederRenderer, BreederTileentity> {

    public BreederItemRenderer() {
        super(BreederRenderer::new, BreederTileentity::new);
    }

}
