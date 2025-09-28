package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public abstract class BlockRendererBase<T extends FakeWorldTileentity, S extends BlockEntityRenderState> implements BlockEntityRenderer<T, S> {

    protected static final Minecraft minecraft = Minecraft.getInstance();
    protected EntityModelSet entityModelSet;

    public BlockRendererBase(EntityModelSet entityModelSet) {
        this.entityModelSet = entityModelSet;
    }


    public static EntityRendererProvider.Context createEntityRenderer() {
        return new EntityRendererProvider.Context(
                minecraft.getEntityRenderDispatcher(),
                minecraft.getItemModelResolver(),
                minecraft.getMapRenderer(),
                minecraft.getBlockRenderer(),
                minecraft.getResourceManager(),
                minecraft.getEntityModels(),
                minecraft.getEntityRenderDispatcher().equipmentAssets,
                minecraft.getAtlasManager(),
                minecraft.font,
                minecraft.playerSkinRenderCache()
        );
    }

    @Override
    public int getViewDistance() {
        return EasyVillagersMod.CLIENT_CONFIG.blockRenderDistance.get();
    }
}
