package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.resources.IReloadableResourceManager;

public class VillagerRendererBase<T extends FakeWorldTileentity> extends BlockRendererBase<T> {

    protected VillagerRenderer villagerRenderer;

    public VillagerRendererBase(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (villagerRenderer == null) {
            villagerRenderer = new VillagerRenderer(minecraft.getRenderManager(), (IReloadableResourceManager) minecraft.getResourceManager());
        }
        super.render(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

}
