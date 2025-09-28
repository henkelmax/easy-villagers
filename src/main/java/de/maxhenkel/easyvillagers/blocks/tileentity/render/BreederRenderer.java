package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class BreederRenderer extends VillagerRendererBase<BreederTileentity, BreederRenderState> {

    private WeakReference<BedRenderer> bedRendererCache = new WeakReference<>(null);

    private final MaterialSet materialSet;

    public BreederRenderer(EntityModelSet entityModelSet, MaterialSet materialSet) {
        super(entityModelSet);
        this.materialSet = materialSet;
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

        state.bedRenderStateBottom.lightCoords = getLightOrDefault(breeder, state);
        state.bedRenderStateTop.lightCoords = getLightOrDefault(breeder, state);
    }

    @Override
    public void submit(BreederRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();

        BedRenderer bedRenderer = bedRendererCache.get();
        if (bedRenderer == null) {
            bedRenderer = new BedRenderer(materialSet, entityModelSet);
            bedRendererCache = new WeakReference<>(bedRenderer);
        }

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
        bedRenderer.submit(state.bedRenderStateBottom, stack, collector, cameraRenderState);
        stack.translate(0D, 0D, -1D);
        bedRenderer.submit(state.bedRenderStateTop, stack, collector, cameraRenderState);
        stack.popPose();

        stack.popPose();
    }

}
