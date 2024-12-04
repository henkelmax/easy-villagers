package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.IronFarmTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;

import java.lang.ref.WeakReference;

public class IronFarmRenderer extends VillagerRendererBase<IronFarmTileentity> {

    private WeakReference<Zombie> zombieCache = new WeakReference<>(null);
    private WeakReference<ZombieRenderer> zombieRendererCache = new WeakReference<>(null);
    private WeakReference<IronGolem> ironGolemCache = new WeakReference<>(null);
    private WeakReference<IronGolemRenderer> ironGolemRendererCache = new WeakReference<>(null);

    protected ZombieRenderState zombieRenderState;
    protected IronGolemRenderState ironGolemRenderState;

    public IronFarmRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public void render(IronFarmTileentity farm, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(farm, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pushPose();

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

        Direction direction = Direction.SOUTH;
        if (!farm.isFakeWorld()) {
            direction = farm.getBlockState().getValue(TraderBlock.FACING);
        }

        if (farm.getVillagerEntity() != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(-5D / 16D, 0D, -5D / 16D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
            matrixStack.scale(0.3F, 0.3F, 0.3F);
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.render(getVillagerRenderState(villagerRenderer, farm.getVillagerEntity()), matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        matrixStack.translate(5D / 16D, 0D, -5D / 16D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-90));
        matrixStack.scale(0.3F, 0.3F, 0.3F);
        zombieRenderer.render(getZombieRenderState(zombieRenderer, zombie), matrixStack, buffer, combinedLight);
        matrixStack.popPose();

        if (farm.getTimer() >= IronFarmTileentity.getGolemSpawnTime() && farm.getTimer() < IronFarmTileentity.getGolemKillTime()) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(0D, 0D, 3D / 16D);
            matrixStack.scale(0.3F, 0.3F, 0.3F);
            if (farm.getTimer() % 20 < 10) {
                ironGolem.hurtTime = 20;
            } else {
                ironGolem.hurtTime = 0;
            }
            ironGolemRenderer.render(getIronGolemRenderState(ironGolemRenderer, ironGolem), matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

    protected ZombieRenderState getZombieRenderState(ZombieRenderer renderer, Zombie zombie) {
        if (zombieRenderState == null) {
            zombieRenderState = renderer.createRenderState();
        }
        renderer.extractRenderState(zombie, zombieRenderState, 0F);
        return zombieRenderState;
    }

    protected IronGolemRenderState getIronGolemRenderState(IronGolemRenderer renderer, IronGolem golem) {
        if (ironGolemRenderState == null) {
            ironGolemRenderState = renderer.createRenderState();
        }
        renderer.extractRenderState(golem, ironGolemRenderState, 0F);
        return ironGolemRenderState;
    }

}
