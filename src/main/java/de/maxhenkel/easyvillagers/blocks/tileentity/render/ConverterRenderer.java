package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.ZombieVillagerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;

public class ConverterRenderer extends VillagerRendererBase<ConverterTileentity> {

    private Zombie zombie;
    private ZombieRenderer zombieRenderer;
    private ZombieVillagerRenderer zombieVillagerRenderer;
    private ZombieVillager zombieVillager;

    public ConverterRenderer(BlockEntityRendererProvider.Context renderer) {
        super(renderer);
    }

    @Override
    public void render(ConverterTileentity converter, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(converter, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pushPose();

        if (zombieRenderer == null) {
            zombieRenderer = new ZombieRenderer(getEntityRenderer());
            zombie = new Zombie(minecraft.level);
        }

        if (zombieVillagerRenderer == null) {
            zombieVillagerRenderer = new ZombieVillagerRenderer(getEntityRenderer());
            zombieVillager = new ZombieVillager(EntityType.ZOMBIE_VILLAGER, minecraft.level);
        }

        Direction direction = Direction.SOUTH;
        if (!converter.isFakeWorld()) {
            direction = converter.getBlockState().getValue(TraderBlock.FACING);
        }
        EasyVillagerEntity villagerEntity = converter.getVillagerEntity();
        if (villagerEntity != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(-5D / 16D, 0D, 0D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
            matrixStack.scale(0.4F, 0.4F, 0.4F);
            if (converter.getTimer() >= ConverterTileentity.getZombifyTime() && converter.getTimer() < ConverterTileentity.getConvertTime()) {
                zombieVillager.setVillagerData(villagerEntity.getVillagerData());
                zombieVillager.setBaby(villagerEntity.isBaby());
                zombieVillagerRenderer.render(zombieVillager, 0F, 1F, matrixStack, buffer, combinedLight);
            } else {
                villagerRenderer.render(villagerEntity, 0F, 1F, matrixStack, buffer, combinedLight);
            }
            matrixStack.popPose();
        }

        matrixStack.pushPose();

        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
        matrixStack.translate(5D / 16D, 0D, 0D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90));
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        zombieRenderer.render(zombie, 0F, 1F, matrixStack, buffer, combinedLight);
        matrixStack.popPose();

        matrixStack.popPose();
    }

}
