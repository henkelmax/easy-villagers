package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class IncubatorRenderer extends TileEntityRenderer<IncubatorTileentity> {

    private Minecraft minecraft;
    private VillagerRenderer renderer;

    public IncubatorRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(IncubatorTileentity incubator, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.push();

        if (renderer == null) {
            renderer = new VillagerRenderer(minecraft.getRenderManager(), (IReloadableResourceManager) minecraft.getResourceManager());
        }

        Direction direction = Direction.SOUTH;
        if (!incubator.isFakeWorld()) {
            direction = incubator.getBlockState().get(TraderBlock.FACING);
        }

        if (incubator.getVillagerEntity() != null) {
            matrixStack.push();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            renderer.render(incubator.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLightIn);
            matrixStack.pop();
        }

        matrixStack.pop();
    }

}
