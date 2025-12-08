package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.ZombieVillagerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class ConverterRenderer extends VillagerRendererBase<ConverterTileentity, ConverterRenderState> {

    private WeakReference<Zombie> zombieCache = new WeakReference<>(null);
    private WeakReference<ZombieRenderer> zombieRendererCache = new WeakReference<>(null);
    private WeakReference<ZombieVillagerRenderer> zombieVillagerRendererCache = new WeakReference<>(null);
    private WeakReference<ZombieVillager> zombieVillagerCache = new WeakReference<>(null);

    public ConverterRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public ConverterRenderState createRenderState() {
        return new ConverterRenderState();
    }

    @Override
    public void extractRenderState(ConverterTileentity converter, ConverterRenderState state, float partialTicks, Vec3 pos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(converter, state, partialTicks, pos, crumblingOverlay);
        state.apply(converter);

        state.renderVillager = false;
        state.renderZombieVillager = false;

        EasyVillagerEntity villagerEntity = converter.getVillagerEntity();
        if (villagerEntity != null) {
            if (converter.getTimer() >= ConverterTileentity.getZombifyTime() && converter.getTimer() < ConverterTileentity.getConvertTime()) {
                state.renderZombieVillager = true;
                state.zombieVillagerRenderState.villagerData = villagerEntity.getVillagerData();
                state.zombieVillagerRenderState.isBaby = villagerEntity.isBaby();
                state.zombieVillagerRenderState.lightCoords = getLightOrDefault(converter, state);
            } else {
                state.renderVillager = true;
                VillagerRenderer villagerRenderer = getVillagerRenderer();
                villagerRenderer.extractRenderState(villagerEntity, state.villagerRenderState, partialTicks);
                state.villagerRenderState.lightCoords = getLightOrDefault(converter, state);
            }
        }

        state.zombieRenderState.lightCoords = getLightOrDefault(converter, state);
    }

    @Override
    public void submit(ConverterRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();

        ZombieRenderer zombieRenderer = zombieRendererCache.get();
        if (zombieRenderer == null) {
            zombieRenderer = new ZombieRenderer(createEntityRenderer());
            zombieRendererCache = new WeakReference<>(zombieRenderer);
        }
        Zombie zombie = zombieCache.get();
        if (zombie == null) {
            zombie = new Zombie(minecraft.level);
            zombieCache = new WeakReference<>(zombie);
        }

        ZombieVillagerRenderer zombieVillagerRenderer = zombieVillagerRendererCache.get();
        if (zombieVillagerRenderer == null) {
            zombieVillagerRenderer = new ZombieVillagerRenderer(createEntityRenderer());
            zombieVillagerRendererCache = new WeakReference<>(zombieVillagerRenderer);
        }
        ZombieVillager zombieVillager = zombieVillagerCache.get();
        if (zombieVillager == null) {
            zombieVillager = new ZombieVillager(EntityType.ZOMBIE_VILLAGER, minecraft.level);
            zombieVillagerCache = new WeakReference<>(zombieVillager);
        }

        if (state.renderZombieVillager || state.renderVillager) {
            stack.pushPose();
            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(-5D / 16D, 0D, 0D);
            stack.mulPose(Axis.YP.rotationDegrees(90));
            stack.scale(0.4F, 0.4F, 0.4F);
            if (state.renderZombieVillager) {
                zombieVillagerRenderer.submit(state.zombieVillagerRenderState, stack, collector, cameraRenderState);
            } else {
                VillagerRenderer villagerRenderer = getVillagerRenderer();
                villagerRenderer.submit(state.villagerRenderState, stack, collector, cameraRenderState);
            }
            stack.popPose();
        }

        stack.pushPose();

        stack.translate(0.5D, 1D / 16D, 0.5D);
        stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
        stack.translate(5D / 16D, 0D, 0D);
        stack.mulPose(Axis.YP.rotationDegrees(-90));
        stack.scale(0.4F, 0.4F, 0.4F);
        zombieRenderer.submit(state.zombieRenderState, stack, collector, cameraRenderState);
        stack.popPose();

        stack.popPose();
    }

}
