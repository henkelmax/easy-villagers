package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

public class BlockRendererBase<T extends FakeWorldTileentity> implements BlockEntityRenderer<T> {

    protected Minecraft minecraft;
    protected BlockEntityRendererProvider.Context renderer;

    public BlockRendererBase(BlockEntityRendererProvider.Context renderer) {
        this.renderer = renderer;
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(T tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        renderBlock(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    protected void renderBlock(T tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState state = tileEntity.getBlockState();
        int color = minecraft.getBlockColors().getColor(state, null, null, 0);
        BlockRenderDispatcher dispatcher = minecraft.getBlockRenderer();
        dispatcher.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderType.cutoutMipped()), state, dispatcher.getBlockModel(state), RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
    }

    public EntityRendererProvider.Context getEntityRenderer() {
        return new EntityRendererProvider.Context(minecraft.getEntityRenderDispatcher(), minecraft.getItemRenderer(), minecraft.getResourceManager(), minecraft.getEntityModels(), minecraft.font);
    }

}
