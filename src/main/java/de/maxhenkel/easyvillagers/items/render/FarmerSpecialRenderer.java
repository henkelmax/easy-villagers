package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.FarmerRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.FarmerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FarmerSpecialRenderer extends ItemSpecialRendererBase<FarmerTileentity, FarmerRenderState> {

    public FarmerSpecialRenderer(EntityModelSet modelSet, BlockModelResolver blockModelResolver, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, FarmerTileentity.class);
        renderer = new FarmerRenderer(modelSet, blockModelResolver);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked<FarmerTileentity> {

        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<FarmerTileentity> bake(BakingContext context) {
            return new FarmerSpecialRenderer(context.entityModelSet(), Minecraft.getInstance().getBlockModelResolver(), () -> ModBlocks.FARMER.get().defaultBlockState());
        }

        @Override
        public MapCodec<FarmerSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

