package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.AutoTraderRenderer;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class AutoTraderSpecialRenderer extends ItemSpecialRendererBase<AutoTraderTileentity, TraderRenderState> {

    public AutoTraderSpecialRenderer(EntityModelSet modelSet, BlockModelResolver blockModelResolver, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, AutoTraderTileentity.class);
        renderer = new AutoTraderRenderer(modelSet, blockModelResolver);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked<AutoTraderTileentity> {

        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<AutoTraderTileentity> bake(BakingContext context) {
            return new AutoTraderSpecialRenderer(context.entityModelSet(), Minecraft.getInstance().getBlockModelResolver(), () -> ModBlocks.AUTO_TRADER.get().defaultBlockState());
        }

        @Override
        public MapCodec<AutoTraderSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

