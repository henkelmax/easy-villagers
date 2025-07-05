package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.InventoryViewerRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class InventoryViewerSpecialRenderer extends ItemSpecialRendererBase<InventoryViewerTileentity> {

    public InventoryViewerSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(modelSet, blockSupplier, InventoryViewerTileentity.class);
        renderer = new InventoryViewerRenderer(modelSet);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<InventoryViewerSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(InventoryViewerSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<InventoryViewerSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new InventoryViewerSpecialRenderer(modelSet, () -> ModBlocks.INVENTORY_VIEWER.get().defaultBlockState());
        }
    }
}

