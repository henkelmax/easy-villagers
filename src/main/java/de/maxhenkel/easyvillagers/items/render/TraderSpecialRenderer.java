package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class TraderSpecialRenderer extends ItemSpecialRendererBase<TraderTileentity, TraderRenderState> {

    public TraderSpecialRenderer(EntityModelSet modelSet, BlockRenderDispatcher blockRenderer, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, TraderTileentity.class);
        renderer = new TraderRenderer(modelSet, blockRenderer);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<TraderSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(TraderSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new TraderSpecialRenderer(context.entityModelSet(), Minecraft.getInstance().getBlockRenderer(), () -> ModBlocks.TRADER.get().defaultBlockState());
        }

        @Override
        public MapCodec<TraderSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

