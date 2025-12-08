package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.tileentity.IronFarmTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class IronFarmRenderer extends VillagerRendererBase<IronFarmTileentity, IronFarmRenderState> {

    private WeakReference<Zombie> zombieCache = new WeakReference<>(null);
    private WeakReference<ZombieRenderer> zombieRendererCache = new WeakReference<>(null);
    private WeakReference<IronGolem> ironGolemCache = new WeakReference<>(null);
    private WeakReference<IronGolemRenderer> ironGolemRendererCache = new WeakReference<>(null);

    public IronFarmRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public IronFarmRenderState createRenderState() {
        return new IronFarmRenderState();
    }

    @Override
    public void extractRenderState(IronFarmTileentity ironFarm, IronFarmRenderState state, float partialTicks, Vec3 pos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(ironFarm, state, partialTicks, pos, crumblingOverlay);

        state.apply(ironFarm);

        state.renderVillager = false;
        if (ironFarm.getVillagerEntity() != null) {
            state.renderVillager = true;
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.extractRenderState(ironFarm.getVillagerEntity(), state.villagerRenderState, partialTicks);
            state.villagerRenderState.lightCoords = getLightOrDefault(ironFarm, state);
        }

        state.renderIronGolem = false;
        if (ironFarm.getTimer() >= IronFarmTileentity.getGolemSpawnTime() && ironFarm.getTimer() < IronFarmTileentity.getGolemKillTime()) {
            state.renderIronGolem = true;
            state.ironGolemRenderState.hasRedOverlay = ironFarm.getTimer() % 20 < 10;
            state.ironGolemRenderState.lightCoords = getLightOrDefault(ironFarm, state);
        }

        state.zombieRenderState.lightCoords = getLightOrDefault(ironFarm, state);
    }

    @Override
    public void submit(IronFarmRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();

        Zombie zombie = zombieCache.get();
        if (zombie == null) {
            zombie = new Zombie(minecraft.level);
            zombieCache = new WeakReference<>(zombie);
        }

        ZombieRenderer zombieRenderer = zombieRendererCache.get();
        if (zombieRenderer == null) {
            zombieRenderer = new ZombieRenderer(createEntityRenderer());
            zombieRendererCache = new WeakReference<>(zombieRenderer);
        }

        IronGolem ironGolem = ironGolemCache.get();
        if (ironGolem == null) {
            ironGolem = new IronGolem(EntityType.IRON_GOLEM, minecraft.level);
            ironGolemCache = new WeakReference<>(ironGolem);
        }

        IronGolemRenderer ironGolemRenderer = ironGolemRendererCache.get();
        if (ironGolemRenderer == null) {
            ironGolemRenderer = new IronGolemRenderer(createEntityRenderer());
            ironGolemRendererCache = new WeakReference<>(ironGolemRenderer);
        }

        if (state.renderVillager) {
            stack.pushPose();
            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(-5D / 16D, 0D, -5D / 16D);
            stack.mulPose(Axis.YP.rotationDegrees(90));
            stack.scale(0.3F, 0.3F, 0.3F);
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.submit(state.villagerRenderState, stack, collector, cameraRenderState);
            stack.popPose();
        }

        stack.pushPose();
        stack.translate(0.5D, 1D / 16D, 0.5D);
        stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
        stack.translate(5D / 16D, 0D, -5D / 16D);
        stack.mulPose(Axis.YP.rotationDegrees(-90));
        stack.scale(0.3F, 0.3F, 0.3F);
        zombieRenderer.submit(state.zombieRenderState, stack, collector, cameraRenderState);
        stack.popPose();

        if (state.renderIronGolem) {
            stack.pushPose();
            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(0D, 0D, 3D / 16D);
            stack.scale(0.3F, 0.3F, 0.3F);
            ironGolemRenderer.submit(state.ironGolemRenderState, stack, collector, cameraRenderState);
            stack.popPose();
        }

        stack.popPose();
    }

}
