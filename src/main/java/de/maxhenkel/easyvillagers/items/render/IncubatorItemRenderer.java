package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.IncubatorRenderer;

public class IncubatorItemRenderer extends BlockItemRendererBase<IncubatorRenderer, IncubatorTileentity> {

    public IncubatorItemRenderer() {
        super(IncubatorRenderer::new, IncubatorTileentity::new);
    }

}
