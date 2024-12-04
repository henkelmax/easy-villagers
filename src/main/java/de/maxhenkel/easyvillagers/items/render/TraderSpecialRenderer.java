package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class TraderSpecialRenderer extends ItemSpecialRendererBase<TraderTileentity> {

    public TraderSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier, Supplier<TraderTileentity> blockEntitySupplier) {
        super(modelSet, blockSupplier, blockEntitySupplier);
        renderer = new TraderRenderer(modelSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<TraderSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(TraderSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<TraderSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new TraderSpecialRenderer(modelSet, () -> ModBlocks.TRADER.get().defaultBlockState(), () -> new TraderTileentity(BlockPos.ZERO, ModBlocks.TRADER.get().defaultBlockState()));
        }
    }
}

