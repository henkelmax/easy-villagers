package de.maxhenkel.easyvillagers.items.render;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.InventoryViewerRenderer;
import net.minecraft.core.BlockPos;

public class InventoryViewerItemRenderer extends BlockItemRendererBase<InventoryViewerRenderer, InventoryViewerTileentity> {

    public InventoryViewerItemRenderer() {
        super(InventoryViewerRenderer::new, () -> new InventoryViewerTileentity(BlockPos.ZERO, ModBlocks.INVENTORY_VIEWER.get().defaultBlockState()));
    }

}
