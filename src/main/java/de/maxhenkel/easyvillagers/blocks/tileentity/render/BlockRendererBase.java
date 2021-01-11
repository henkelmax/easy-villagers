package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.client.model.data.EmptyModelData;

public class BlockRendererBase<T extends FakeWorldTileentity> extends TileEntityRenderer<T> {

    protected Minecraft minecraft;

    public BlockRendererBase(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(T tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        renderBlock(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    protected void renderBlock(T tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        BlockState state = tileEntity.getBlockState();
        BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
        dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getCutoutMipped()), state, dispatcher.getModelForState(state), 0, 0, 0, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
    }

}
