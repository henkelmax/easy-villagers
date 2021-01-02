package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.resources.IReloadableResourceManager;

public class AutoTraderRenderer extends TileEntityRenderer<AutoTraderTileentity> {

    private Minecraft minecraft;
    private VillagerRenderer renderer;

    public AutoTraderRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(AutoTraderTileentity trader, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = new VillagerRenderer(minecraft.getRenderManager(), (IReloadableResourceManager) minecraft.getResourceManager());
        }
        TraderRenderer.renderTraderBase(minecraft, renderer, trader, partialTicks, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
