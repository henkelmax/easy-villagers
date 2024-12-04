package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.InventoryViewerRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class InventoryViewerSpecialRenderer extends ItemSpecialRendererBase<InventoryViewerTileentity> {

    public InventoryViewerSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier, Supplier<InventoryViewerTileentity> blockEntitySupplier) {
        super(modelSet, blockSupplier, blockEntitySupplier);
        renderer = new InventoryViewerRenderer(modelSet);
    }

    @OnlyIn(Dist.CLIENT)
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
            return new InventoryViewerSpecialRenderer(modelSet, () -> ModBlocks.INVENTORY_VIEWER.get().defaultBlockState(), () -> new InventoryViewerTileentity(BlockPos.ZERO, ModBlocks.INVENTORY_VIEWER.get().defaultBlockState()));
        }
    }
}
