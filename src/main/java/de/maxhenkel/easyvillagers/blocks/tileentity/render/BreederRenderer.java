package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BedBlockEntity;

import java.lang.ref.WeakReference;

public class BreederRenderer extends VillagerRendererBase<BreederTileentity> {

    private WeakReference<BedRenderer> bedRendererCache = new WeakReference<>(null);
    private WeakReference<BedBlockEntity> bedCache = new WeakReference<>(null);

    public BreederRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public void render(BreederTileentity breeder, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(breeder, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pushPose();

        BedRenderer bedRenderer = bedRendererCache.get();
        if (bedRenderer == null) {
            bedRenderer = new BedRenderer(entityModelSet);
            bedRendererCache = new WeakReference<>(bedRenderer);
        }

        BedBlockEntity bed = bedCache.get();
        if (bed == null) {
            bed = new BedBlockEntity(BlockPos.ZERO, Blocks.RED_BED.defaultBlockState());
            bedCache = new WeakReference<>(bed);
        }

        VillagerRenderer villagerRenderer = getVillagerRenderer();

        Direction direction = Direction.SOUTH;
        if (!breeder.isFakeWorld()) {
            direction = breeder.getBlockState().getValue(TraderBlock.FACING);
        }

        if (breeder.getVillagerEntity1() != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(-5D / 16D, 0D, 0D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.render(getVillagerRenderState(villagerRenderer, breeder.getVillagerEntity1()), matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        if (breeder.getVillagerEntity2() != null) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(5D / 16D, 0D, 0D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-90));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.render(getVillagerRenderState(villagerRenderer, breeder.getVillagerEntity2()), matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        matrixStack.translate(0D, 0D, 3D / 16D);
        matrixStack.translate(-0.5D, 0D, -0.5D);
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        matrixStack.translate(0.5D / 0.4D - 0.5D, 0D, 0.5D / 0.4D - 0.5D);
        bedRenderer.render(bed, 1F, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.popPose();

        matrixStack.popPose();
    }

}
