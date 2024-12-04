package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.FarmerRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class FarmerSpecialRenderer extends ItemSpecialRendererBase<FarmerTileentity> {

    public FarmerSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier, Supplier<FarmerTileentity> blockEntitySupplier) {
        super(modelSet, blockSupplier, blockEntitySupplier);
        renderer = new FarmerRenderer(modelSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<FarmerSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(FarmerSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<FarmerSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new FarmerSpecialRenderer(modelSet, () -> ModBlocks.FARMER.get().defaultBlockState(), () -> new FarmerTileentity(BlockPos.ZERO, ModBlocks.FARMER.get().defaultBlockState()));
        }
    }
}
