package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.AutoTraderRenderer;
import net.minecraft.core.BlockPos;

public class AutoTraderItemRenderer extends BlockItemRendererBase<AutoTraderRenderer, AutoTraderTileentity> {

    public AutoTraderItemRenderer() {
        super(AutoTraderRenderer::new, () -> new AutoTraderTileentity(BlockPos.ZERO, ModBlocks.AUTO_TRADER.defaultBlockState()));
    }

}
