package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class IncubatorRenderer extends VillagerRendererBase<IncubatorTileentity, IncubatorRenderState> {

    public IncubatorRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public IncubatorRenderState createRenderState() {
        return new IncubatorRenderState();
    }

    @Override
    public void extractRenderState(IncubatorTileentity incubator, IncubatorRenderState state, float partialTicks, Vec3 pos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(incubator, state, partialTicks, pos, crumblingOverlay);

        state.apply(incubator);

        state.renderVillager = false;
        if (incubator.getVillagerEntity() != null) {
            state.renderVillager = true;
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.extractRenderState(incubator.getVillagerEntity(), state.villagerRenderState, partialTicks);
            state.villagerRenderState.lightCoords = getLightOrDefault(incubator, state);
        }
    }

    @Override
    public void submit(IncubatorRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();

        if (state.renderVillager) {
            stack.pushPose();

            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.scale(0.45F, 0.45F, 0.45F);
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.submit(state.villagerRenderState, stack, collector, cameraRenderState);
            stack.popPose();
        }

        stack.popPose();
    }

}
