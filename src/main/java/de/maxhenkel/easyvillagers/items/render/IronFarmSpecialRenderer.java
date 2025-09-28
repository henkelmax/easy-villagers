package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.IronFarmTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.IronFarmRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.IronFarmRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class IronFarmSpecialRenderer extends ItemSpecialRendererBase<IronFarmTileentity, IronFarmRenderState> {

    public IronFarmSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, IronFarmTileentity.class);
        renderer = new IronFarmRenderer(modelSet);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<IronFarmSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(IronFarmSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new IronFarmSpecialRenderer(context.entityModelSet(), () -> ModBlocks.IRON_FARM.get().defaultBlockState());
        }

        @Override
        public MapCodec<IronFarmSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

