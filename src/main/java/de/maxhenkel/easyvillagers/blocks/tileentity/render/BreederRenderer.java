package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BedBlockEntity;

public class BreederRenderer extends VillagerRendererBase<BreederTileentity> {

    private BedRenderer bedRenderer;
    private BedBlockEntity bed;

    public BreederRenderer(BlockEntityRendererProvider.Context renderer) {
        super(renderer);
    }

    @Override
    public void render(BreederTileentity breeder, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(breeder, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pushPose();

        if (bedRenderer == null) {
            bedRenderer = new BedRenderer(renderer);
            bed = new BedBlockEntity(BlockPos.ZERO, Blocks.RED_BED.defaultBlockState());
        }

        Direction direction = Direction.SOUTH;
        if (!breeder.isFakeWorld()) {
            direction = breeder.getBlockState().getValue(TraderBlock.FACING);
        }

        if (breeder.getVillagerEntity1() != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(-5D / 16D, 0D, 0D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.render(breeder.getVillagerEntity1(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        if (breeder.getVillagerEntity2() != null) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(5D / 16D, 0D, 0D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.render(breeder.getVillagerEntity2(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
        matrixStack.translate(0D, 0D, 3D / 16D);
        matrixStack.translate(-0.5D, 0D, -0.5D);
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        matrixStack.translate(0.5D / 0.4D - 0.5D, 0D, 0.5D / 0.4D - 0.5D);
        bedRenderer.render(bed, 1F, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.popPose();

        matrixStack.popPose();
    }

}
