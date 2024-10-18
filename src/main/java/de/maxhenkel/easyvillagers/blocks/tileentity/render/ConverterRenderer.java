package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.ZombieVillagerRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;

import java.lang.ref.WeakReference;

public class ConverterRenderer extends VillagerRendererBase<ConverterTileentity> {

    private WeakReference<Zombie> zombieCache = new WeakReference<>(null);
    private WeakReference<ZombieRenderer> zombieRendererCache = new WeakReference<>(null);
    private WeakReference<ZombieVillagerRenderer> zombieVillagerRendererCache = new WeakReference<>(null);
    private WeakReference<ZombieVillager> zombieVillagerCache = new WeakReference<>(null);

    protected ZombieVillagerRenderState zombieVillagerRenderState;
    protected ZombieRenderState zombieRenderState;

    public ConverterRenderer(BlockEntityRendererProvider.Context renderer) {
        super(renderer);
    }

    @Override
    public void render(ConverterTileentity converter, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(converter, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pushPose();

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

        Direction direction = Direction.SOUTH;
        if (!converter.isFakeWorld()) {
            direction = converter.getBlockState().getValue(TraderBlock.FACING);
        }
        EasyVillagerEntity villagerEntity = converter.getVillagerEntity();
        if (villagerEntity != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(-5D / 16D, 0D, 0D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
            matrixStack.scale(0.4F, 0.4F, 0.4F);
            if (converter.getTimer() >= ConverterTileentity.getZombifyTime() && converter.getTimer() < ConverterTileentity.getConvertTime()) {
                zombieVillager.setVillagerData(villagerEntity.getVillagerData());
                zombieVillager.setBaby(villagerEntity.isBaby());
                zombieVillagerRenderer.render(getZombieVillagerRenderState(zombieVillagerRenderer, zombieVillager), matrixStack, buffer, combinedLight);
            } else {
                VillagerRenderer villagerRenderer = getVillagerRenderer();
                villagerRenderer.render(getVillagerRenderState(villagerRenderer, villagerEntity), matrixStack, buffer, combinedLight);
            }
            matrixStack.popPose();
        }

        matrixStack.pushPose();

        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        matrixStack.translate(5D / 16D, 0D, 0D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-90));
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        zombieRenderer.render(getZombieRenderState(zombieRenderer, zombie), matrixStack, buffer, combinedLight);
        matrixStack.popPose();

        matrixStack.popPose();
    }

    protected ZombieVillagerRenderState getZombieVillagerRenderState(ZombieVillagerRenderer renderer, ZombieVillager villager) {
        if (zombieVillagerRenderState == null) {
            zombieVillagerRenderState = renderer.createRenderState();
        }
        renderer.extractRenderState(villager, zombieVillagerRenderState, 0F);
        return zombieVillagerRenderState;
    }

    protected ZombieRenderState getZombieRenderState(ZombieRenderer renderer, Zombie zombie) {
        if (zombieRenderState == null) {
            zombieRenderState = renderer.createRenderState();
        }
        renderer.extractRenderState(zombie, zombieRenderState, 0F);
        return zombieRenderState;
    }

}
