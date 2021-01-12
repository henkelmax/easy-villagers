package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class IncubatorRenderer extends VillagerRendererBase<IncubatorTileentity> {

    public IncubatorRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(IncubatorTileentity incubator, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        super.render(incubator, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.push();

        Direction direction = Direction.SOUTH;
        if (!incubator.isFakeWorld()) {
            direction = incubator.getBlockState().get(TraderBlock.FACING);
        }

        if (incubator.getVillagerEntity() != null) {
            matrixStack.push();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.render(incubator.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.pop();
        }

        matrixStack.pop();
    }

}
