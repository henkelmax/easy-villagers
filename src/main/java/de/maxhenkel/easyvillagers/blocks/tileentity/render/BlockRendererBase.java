package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BlockRendererBase<T extends FakeWorldTileentity> implements BlockEntityRenderer<T> {

    protected Minecraft minecraft;
    protected BlockEntityRendererProvider.Context renderer;

    public BlockRendererBase(BlockEntityRendererProvider.Context renderer) {
        this.renderer = renderer;
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(T tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

    }

    public EntityRendererProvider.Context createEntityRenderer() {
        return new EntityRendererProvider.Context(minecraft.getEntityRenderDispatcher(), minecraft.getItemRenderer(), minecraft.getBlockRenderer(), minecraft.gameRenderer.itemInHandRenderer, minecraft.getResourceManager(), minecraft.getEntityModels(), minecraft.font);
    }

    @Override
    public int getViewDistance() {
        return Main.CLIENT_CONFIG.blockRenderDistance.get();
    }
}
