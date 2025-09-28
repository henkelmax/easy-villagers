package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.AutoTraderRenderer;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class AutoTraderSpecialRenderer extends ItemSpecialRendererBase<AutoTraderTileentity, TraderRenderState> {

    public AutoTraderSpecialRenderer(EntityModelSet modelSet, BlockRenderDispatcher blockRenderer, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, AutoTraderTileentity.class);
        renderer = new AutoTraderRenderer(modelSet, blockRenderer);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<AutoTraderSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(AutoTraderSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new AutoTraderSpecialRenderer(context.entityModelSet(), Minecraft.getInstance().getBlockRenderer(), () -> ModBlocks.AUTO_TRADER.get().defaultBlockState());
        }

        @Override
        public MapCodec<AutoTraderSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

