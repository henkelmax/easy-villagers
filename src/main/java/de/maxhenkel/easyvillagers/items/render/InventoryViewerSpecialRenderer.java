package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.InventoryViewerRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.InventoryViewerRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class InventoryViewerSpecialRenderer extends ItemSpecialRendererBase<InventoryViewerTileentity, InventoryViewerRenderState> {

    public InventoryViewerSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, InventoryViewerTileentity.class);
        renderer = new InventoryViewerRenderer(modelSet);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<InventoryViewerSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(InventoryViewerSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new InventoryViewerSpecialRenderer(context.entityModelSet(), () -> ModBlocks.INVENTORY_VIEWER.get().defaultBlockState());
        }

        @Override
        public MapCodec<InventoryViewerSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

