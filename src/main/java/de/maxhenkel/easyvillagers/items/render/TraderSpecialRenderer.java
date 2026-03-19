package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class TraderSpecialRenderer extends ItemSpecialRendererBase<TraderTileentity, TraderRenderState> {

    public TraderSpecialRenderer(EntityModelSet modelSet, BlockModelResolver blockModelResolver, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, TraderTileentity.class);
        renderer = new TraderRenderer(modelSet, blockModelResolver);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked<TraderTileentity> {

        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<TraderTileentity> bake(BakingContext context) {
            return new TraderSpecialRenderer(context.entityModelSet(), Minecraft.getInstance().getBlockModelResolver(), () -> ModBlocks.TRADER.get().defaultBlockState());
        }

        @Override
        public MapCodec<TraderSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

