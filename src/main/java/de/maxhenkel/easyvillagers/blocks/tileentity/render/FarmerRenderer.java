package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FarmerRenderer extends VillagerRendererBase<FarmerTileentity, FarmerRenderState> {

    private final BlockRenderDispatcher blockRenderer;

    public FarmerRenderer(EntityModelSet entityModelSet, BlockRenderDispatcher blockRenderer) {
        super(entityModelSet);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public FarmerRenderState createRenderState() {
        return new FarmerRenderState();
    }

    @Override
    public void extractRenderState(FarmerTileentity farmer, FarmerRenderState state, float partialTicks, Vec3 pos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(farmer, state, partialTicks, pos, crumblingOverlay);
        state.apply(farmer);

        state.renderVillager = false;
        if (farmer.getVillagerEntity() != null) {
            state.renderVillager = true;
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.extractRenderState(farmer.getVillagerEntity(), state.villagerRenderState, partialTicks);
            state.villagerRenderState.lightCoords = getLightOrDefault(farmer, state);
        }

        state.crop = farmer.getCrop();
    }

    @Override
    public void submit(FarmerRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();

        if (state.renderVillager) {
            stack.pushPose();

            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(0D, 0D, -4D / 16D);
            stack.scale(0.45F, 0.45F, 0.45F);
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.submit(state.villagerRenderState, stack, collector, cameraRenderState);
            stack.popPose();
        }

        BlockState crop = state.crop;
        if (crop != null) {
            stack.pushPose();

            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(0D, 0D, 2D / 16D);
            stack.translate(-0.5D, 0D, -0.5D);
            stack.scale(0.45F, 0.45F, 0.45F);
            stack.translate(0.5D / 0.45D - 0.5D, 0D, 0.5D / 0.45D - 0.5D);

            if (minecraft.level != null) {
                int color = minecraft.getBlockColors().getColor(crop, null, null, 0);
                // See ItemFrameRenderer
                collector.submitBlockModel(
                        stack,
                        RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS),
                        blockRenderer.getBlockModel(crop),
                        RenderUtils.getRedFloat(color),
                        RenderUtils.getGreenFloat(color),
                        RenderUtils.getBlueFloat(color),
                        state.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        0
                );
            }

            stack.popPose();
        }

        stack.popPose();
    }

}
