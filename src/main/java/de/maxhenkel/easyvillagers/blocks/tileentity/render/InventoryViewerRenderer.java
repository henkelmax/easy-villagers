package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class InventoryViewerRenderer extends VillagerRendererBase<InventoryViewerTileentity> {

    public InventoryViewerRenderer(BlockEntityRendererProvider.Context renderer) {
        super(renderer);
    }

    @Override
    public void render(InventoryViewerTileentity inventoryViewer, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(inventoryViewer, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pushPose();

        Direction direction = Direction.SOUTH;
        if (!inventoryViewer.isFakeWorld()) {
            direction = inventoryViewer.getBlockState().getValue(TraderBlock.FACING);
        }

        if (inventoryViewer.getVillagerEntity() != null) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            getVillagerRenderer().render(inventoryViewer.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

}
