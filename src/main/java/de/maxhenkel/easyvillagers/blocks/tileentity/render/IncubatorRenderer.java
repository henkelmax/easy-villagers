package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class IncubatorRenderer extends VillagerRendererBase<IncubatorTileentity> {

    public IncubatorRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public void render(IncubatorTileentity incubator, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, Vec3 vec) {
        super.render(incubator, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay, vec);
        matrixStack.pushPose();

        Direction direction = Direction.SOUTH;
        if (!incubator.isFakeWorld()) {
            direction = incubator.getBlockState().getValue(TraderBlock.FACING);
        }

        if (incubator.getVillagerEntity() != null) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.render(getVillagerRenderState(villagerRenderer, incubator.getVillagerEntity()), matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

}
