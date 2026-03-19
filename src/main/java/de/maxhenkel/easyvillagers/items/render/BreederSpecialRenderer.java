package de.maxhenkel.easyvillagers.items.render;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderState;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BreederSpecialRenderer extends ItemSpecialRendererBase<BreederTileentity, BreederRenderState> {

    public BreederSpecialRenderer(EntityModelSet modelSet, SpriteGetter spriteGetter, Supplier<BlockState> blockSupplier) {
        super(blockSupplier, BreederTileentity.class);
        renderer = new BreederRenderer(modelSet, spriteGetter);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked<BreederTileentity> {

        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<BreederTileentity> bake(BakingContext context) {
            return new BreederSpecialRenderer(context.entityModelSet(), context.sprites(), () -> ModBlocks.BREEDER.get().defaultBlockState());
        }

        @Override
        public MapCodec<BreederSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }
}

