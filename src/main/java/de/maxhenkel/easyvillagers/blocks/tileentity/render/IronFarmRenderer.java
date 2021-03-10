package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.IronFarmTileentity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class IronFarmRenderer extends VillagerRendererBase<IronFarmTileentity> {

    private ZombieEntity zombie;
    private ZombieRenderer zombieRenderer;
    private IronGolemEntity ironGolem;
    private IronGolemRenderer ironGolemRenderer;

    public IronFarmRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(IronFarmTileentity farm, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        super.render(farm, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pushPose();

        if (zombieRenderer == null) {
            zombieRenderer = new ZombieRenderer(minecraft.getEntityRenderDispatcher());
            zombie = new ZombieEntity(minecraft.level);
        }

        if (ironGolemRenderer == null) {
            ironGolemRenderer = new IronGolemRenderer(minecraft.getEntityRenderDispatcher());
            ironGolem = new IronGolemEntity(EntityType.IRON_GOLEM, minecraft.level);
        }

        Direction direction = Direction.SOUTH;
        if (!farm.isFakeWorld()) {
            direction = farm.getBlockState().getValue(TraderBlock.FACING);
        }

        if (farm.getVillagerEntity() != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(-5D / 16D, 0D, -5D / 16D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
            matrixStack.scale(0.3F, 0.3F, 0.3F);
            villagerRenderer.render(farm.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
        matrixStack.translate(5D / 16D, 0D, -5D / 16D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90));
        matrixStack.scale(0.3F, 0.3F, 0.3F);
        zombieRenderer.render(zombie, 0F, 1F, matrixStack, buffer, combinedLight);
        matrixStack.popPose();

        if (farm.getTimer() >= IronFarmTileentity.getGolemSpawnTime() && farm.getTimer() < IronFarmTileentity.getGolemKillTime()) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(0D, 0D, 3D / 16D);
            matrixStack.scale(0.3F, 0.3F, 0.3F);
            if (farm.getTimer() % 20 < 10) {
                ironGolem.hurtTime = 20;
            } else {
                ironGolem.hurtTime = 0;
            }
            ironGolemRenderer.render(ironGolem, 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

}
