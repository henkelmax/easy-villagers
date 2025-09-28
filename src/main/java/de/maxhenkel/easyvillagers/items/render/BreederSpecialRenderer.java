package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BreederSpecialRenderer extends ItemSpecialRendererBase<BreederTileentity, BreederRenderState> {

    public BreederSpecialRenderer(EntityModelSet modelSet, MaterialSet materialSet, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, BreederTileentity.class);
        renderer = new BreederRenderer(modelSet, materialSet);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<BreederSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(BreederSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new BreederSpecialRenderer(context.entityModelSet(), context.materials(), () -> ModBlocks.BREEDER.get().defaultBlockState());
        }

        @Override
        public MapCodec<BreederSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

