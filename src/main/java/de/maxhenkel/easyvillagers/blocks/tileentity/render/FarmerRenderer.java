package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FarmerRenderer extends VillagerRendererBase<FarmerTileentity> {

    public FarmerRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public void render(FarmerTileentity farmer, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, Vec3 vec) {
        super.render(farmer, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay, vec);
        matrixStack.pushPose();

        Direction direction = Direction.SOUTH;
        if (!farmer.isFakeWorld()) {
            direction = farmer.getBlockState().getValue(TraderBlock.FACING);
        }

        if (farmer.getVillagerEntity() != null) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(0D, 0D, -4D / 16D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.render(getVillagerRenderState(villagerRenderer, farmer.getVillagerEntity()), matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        BlockState crop = farmer.getCrop();
        if (crop != null) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(0D, 0D, 2D / 16D);
            matrixStack.translate(-0.5D, 0D, -0.5D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            matrixStack.translate(0.5D / 0.45D - 0.5D, 0D, 0.5D / 0.45D - 0.5D);

            if (minecraft.level != null) {
                BlockRenderDispatcher dispatcher = minecraft.getBlockRenderer();
                int color = minecraft.getBlockColors().getColor(crop, null, null, 0);
                dispatcher.getModelRenderer().renderModel(
                        matrixStack.last(),
                        buffer,
                        dispatcher.getBlockModel(crop),
                        RenderUtils.getRedFloat(color),
                        RenderUtils.getGreenFloat(color),
                        RenderUtils.getBlueFloat(color),
                        combinedLight,
                        combinedOverlay,
                        minecraft.level,
                        BlockPos.ZERO,
                        crop
                );
            }

            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

}
