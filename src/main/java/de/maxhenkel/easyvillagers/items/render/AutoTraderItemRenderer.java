package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.AutoTraderRenderer;

public class AutoTraderItemRenderer extends BlockItemRendererBase<AutoTraderRenderer, AutoTraderTileentity> {

    public AutoTraderItemRenderer() {
        super(AutoTraderRenderer::new, AutoTraderTileentity::new);
    }

}
