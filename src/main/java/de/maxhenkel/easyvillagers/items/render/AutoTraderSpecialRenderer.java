package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.AutoTraderRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class AutoTraderSpecialRenderer extends ItemSpecialRendererBase<AutoTraderTileentity> {

    public AutoTraderSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier, Supplier<AutoTraderTileentity> blockEntitySupplier) {
        super(modelSet, blockSupplier, blockEntitySupplier);
        renderer = new AutoTraderRenderer(modelSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<AutoTraderSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(AutoTraderSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<AutoTraderSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new AutoTraderSpecialRenderer(modelSet, () -> ModBlocks.AUTO_TRADER.get().defaultBlockState(), () -> new AutoTraderTileentity(BlockPos.ZERO, ModBlocks.AUTO_TRADER.get().defaultBlockState()));
        }
    }
}

