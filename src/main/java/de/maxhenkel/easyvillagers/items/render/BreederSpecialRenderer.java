package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class BreederSpecialRenderer extends ItemSpecialRendererBase<BreederTileentity> {

    public BreederSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier, Supplier<BreederTileentity> blockEntitySupplier) {
        super(modelSet, blockSupplier, blockEntitySupplier);
        renderer = new BreederRenderer(modelSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<BreederSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(BreederSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<BreederSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new BreederSpecialRenderer(modelSet, () -> ModBlocks.BREEDER.get().defaultBlockState(), () -> new BreederTileentity(BlockPos.ZERO, ModBlocks.BREEDER.get().defaultBlockState()));
        }
    }
}

