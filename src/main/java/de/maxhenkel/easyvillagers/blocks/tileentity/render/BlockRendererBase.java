package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class BlockRendererBase<T extends FakeWorldTileentity> implements BlockEntityRenderer<T> {

    protected static final Minecraft minecraft = Minecraft.getInstance();
    protected EntityModelSet entityModelSet;

    public BlockRendererBase(EntityModelSet entityModelSet) {
        this.entityModelSet = entityModelSet;
    }

    @Override
    public void render(T tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, Vec3 vec) {

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
                minecraft.font
        );
    }

    @Override
    public int getViewDistance() {
        return EasyVillagersMod.CLIENT_CONFIG.blockRenderDistance.get();
    }
}
