package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.FarmerRenderer;

public class FarmerItemRenderer extends BlockItemRendererBase<FarmerRenderer, FarmerTileentity> {

    public FarmerItemRenderer() {
        super(FarmerRenderer::new, FarmerTileentity::new);
    }

}
