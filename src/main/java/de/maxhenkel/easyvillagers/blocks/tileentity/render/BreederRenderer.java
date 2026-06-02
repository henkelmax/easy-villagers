package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BreederRenderer extends VillagerRendererBase<BreederTileentity, BreederRenderState> {

    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelResolver blockModelResolver;

    public BreederRenderer(EntityModelSet entityModelSet, BlockModelResolver blockModelResolver) {
        super(entityModelSet);
        this.blockModelResolver = blockModelResolver;
    }

    @Override
    public BreederRenderState createRenderState() {
        return new BreederRenderState();
    }

    @Override
    public void extractRenderState(BreederTileentity breeder, BreederRenderState state, float partialTicks, Vec3 pos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(breeder, state, partialTicks, pos, crumblingOverlay);

        state.direction = Direction.SOUTH;
        if (!breeder.isFakeWorld()) {
            state.direction = breeder.getBlockState().getValue(TraderBlock.FACING);
        }

        VillagerRenderer villagerRenderer = getVillagerRenderer();

        if (breeder.getVillagerEntity1() != null) {
            state.renderVillager1 = true;
            villagerRenderer.extractRenderState(breeder.getVillagerEntity1(), state.villagerRenderState1, 0F);
            state.villagerRenderState1.lightCoords = getLightOrDefault(breeder, state);
        } else {
            state.renderVillager1 = false;
        }

        if (breeder.getVillagerEntity2() != null) {
            state.renderVillager2 = true;
            villagerRenderer.extractRenderState(breeder.getVillagerEntity2(), state.villagerRenderState2, 0F);
            state.villagerRenderState2.lightCoords = getLightOrDefault(breeder, state);
        } else {
            state.renderVillager2 = false;
        }

        blockModelResolver.update(state.bedFoot, Blocks.BED.red().defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
        blockModelResolver.update(state.bedHead, Blocks.BED.red().defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD), BLOCK_DISPLAY_CONTEXT);
    }

    @Override
    public void submit(BreederRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();
        VillagerRenderer villagerRenderer = getVillagerRenderer();

        if (state.renderVillager1) {
            stack.pushPose();
            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(-5D / 16D, 0D, 0D);
            stack.mulPose(Axis.YP.rotationDegrees(90));
            stack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.submit(state.villagerRenderState1, stack, collector, cameraRenderState);
            stack.popPose();
        }

        if (state.renderVillager2) {
            stack.pushPose();

            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(5D / 16D, 0D, 0D);
            stack.mulPose(Axis.YP.rotationDegrees(-90));
            stack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.submit(state.villagerRenderState2, stack, collector, cameraRenderState);
            stack.popPose();
        }

        stack.pushPose();
        stack.translate(0.5D, 1D / 16D, 0.5D);
        stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
        stack.translate(0D, 0D, 3D / 16D);
        stack.translate(-0.5D, 0D, -0.5D);
        stack.scale(0.4F, 0.4F, 0.4F);
        stack.translate(0.5D / 0.4D - 0.5D, 0D, 0.5D / 0.4D - 0.5D);

        renderBlock(state.bedFoot, state.lightCoords, stack, collector);
        stack.translate(0D, 0D, -1D);
        renderBlock(state.bedHead, state.lightCoords, stack, collector);

        stack.popPose();
        stack.popPose();
    }

    public static void renderBlock(BlockModelRenderState state, int lightCoords, PoseStack stack, SubmitNodeCollector collector) {
        state.submit(stack, collector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }

}
