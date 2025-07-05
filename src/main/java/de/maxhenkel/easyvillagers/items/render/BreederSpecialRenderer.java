package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class BreederSpecialRenderer extends ItemSpecialRendererBase<BreederTileentity> {

    public BreederSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(modelSet, blockSupplier, BreederTileentity.class);
        renderer = new BreederRenderer(modelSet);
    }

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
            return new BreederSpecialRenderer(modelSet, () -> ModBlocks.BREEDER.get().defaultBlockState());
        }
    }
}

