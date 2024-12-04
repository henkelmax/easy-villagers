package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.IncubatorRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class IncubatorSpecialRenderer extends ItemSpecialRendererBase<IncubatorTileentity> {

    public IncubatorSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier, Supplier<IncubatorTileentity> blockEntitySupplier) {
        super(modelSet, blockSupplier, blockEntitySupplier);
        renderer = new IncubatorRenderer(modelSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<IncubatorSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(IncubatorSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<IncubatorSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new IncubatorSpecialRenderer(modelSet, () -> ModBlocks.INCUBATOR.get().defaultBlockState(), () -> new IncubatorTileentity(BlockPos.ZERO, ModBlocks.INCUBATOR.get().defaultBlockState()));
        }
    }
}

