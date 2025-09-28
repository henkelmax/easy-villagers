package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.ConverterRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.ConverterRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ConverterSpecialRenderer extends ItemSpecialRendererBase<ConverterTileentity, ConverterRenderState> {

    public ConverterSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, ConverterTileentity.class);
        renderer = new ConverterRenderer(modelSet);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<ConverterSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(ConverterSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new ConverterSpecialRenderer(context.entityModelSet(), () -> ModBlocks.CONVERTER.get().defaultBlockState());
        }

        @Override
        public MapCodec<ConverterSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

