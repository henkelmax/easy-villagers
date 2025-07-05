package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.FarmerRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class FarmerSpecialRenderer extends ItemSpecialRendererBase<FarmerTileentity> {

    public FarmerSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(modelSet, blockSupplier, FarmerTileentity.class);
        renderer = new FarmerRenderer(modelSet);
    }

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
            return new FarmerSpecialRenderer(modelSet, () -> ModBlocks.FARMER.get().defaultBlockState());
        }
    }
}

