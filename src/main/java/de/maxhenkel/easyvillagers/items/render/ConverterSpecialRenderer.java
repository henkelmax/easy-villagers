package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.ConverterRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ConverterSpecialRenderer extends ItemSpecialRendererBase<ConverterTileentity> {

    public ConverterSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(modelSet, blockSupplier, ConverterTileentity.class);
        renderer = new ConverterRenderer(modelSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<ConverterSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(ConverterSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<ConverterSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new ConverterSpecialRenderer(modelSet, () -> ModBlocks.CONVERTER.get().defaultBlockState());
        }
    }
}

