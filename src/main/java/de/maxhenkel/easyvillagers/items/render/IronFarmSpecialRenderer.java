package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.IronFarmTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.IronFarmRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class IronFarmSpecialRenderer extends ItemSpecialRendererBase<IronFarmTileentity> {

    public IronFarmSpecialRenderer(EntityModelSet modelSet, Supplier<BlockState> blockSupplier) {
        super(modelSet, blockSupplier, IronFarmTileentity.class);
        renderer = new IronFarmRenderer(modelSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<IronFarmSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(IronFarmSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<IronFarmSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new IronFarmSpecialRenderer(modelSet, () -> ModBlocks.IRON_FARM.get().defaultBlockState());
        }
    }
}

