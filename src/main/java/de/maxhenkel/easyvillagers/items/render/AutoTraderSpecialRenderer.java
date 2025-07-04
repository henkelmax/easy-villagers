package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.AutoTraderRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class AutoTraderSpecialRenderer extends ItemSpecialRendererBase<AutoTraderTileentity> {

    public AutoTraderSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(modelSet, blockSupplier, AutoTraderTileentity.class);
        renderer = new AutoTraderRenderer(modelSet);
    }

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
            return new AutoTraderSpecialRenderer(modelSet, () -> ModBlocks.AUTO_TRADER.get().defaultBlockState());
        }
    }
}

