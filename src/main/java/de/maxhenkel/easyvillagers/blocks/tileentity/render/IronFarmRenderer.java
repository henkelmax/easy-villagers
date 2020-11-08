package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.IronFarmTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class IronFarmRenderer extends TileEntityRenderer<IronFarmTileentity> {

    private Minecraft minecraft;
    private VillagerRenderer renderer;
    private ZombieEntity zombie;
    private ZombieRenderer zombieRenderer;
    private IronGolemEntity ironGolem;
    private IronGolemRenderer ironGolemRenderer;

    public IronFarmRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(IronFarmTileentity farm, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.push();

        if (renderer == null) {
            renderer = new VillagerRenderer(minecraft.getRenderManager(), (IReloadableResourceManager) minecraft.getResourceManager());
        }

        if (zombieRenderer == null) {
            zombieRenderer = new ZombieRenderer(minecraft.getRenderManager());
            zombie = new ZombieEntity(minecraft.world);
        }

        if (ironGolemRenderer == null) {
            ironGolemRenderer = new IronGolemRenderer(minecraft.getRenderManager());
            ironGolem = new IronGolemEntity(EntityType.IRON_GOLEM, minecraft.world);
        }

        Direction direction = Direction.SOUTH;
        if (!farm.isFakeWorld()) {
            direction = farm.getBlockState().get(TraderBlock.FACING);
        }

        if (farm.getVillagerEntity() != null) {
            matrixStack.push();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.translate(-5D / 16D, 0D, -5D / 16D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStack.scale(0.3F, 0.3F, 0.3F);
            renderer.render(farm.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLightIn);
            matrixStack.pop();
        }

        matrixStack.push();
        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
        matrixStack.translate(5D / 16D, 0D, -5D / 16D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90));
        matrixStack.scale(0.3F, 0.3F, 0.3F);
        zombieRenderer.render(zombie, 0F, 1F, matrixStack, buffer, combinedLightIn);
        matrixStack.pop();

        if (farm.getTimer() >= IronFarmTileentity.getGolemSpawnTime() && farm.getTimer() < IronFarmTileentity.getGolemKillTime()) {
            matrixStack.push();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.translate(0D, 0D, 3D / 16D);
            matrixStack.scale(0.3F, 0.3F, 0.3F);
            if (farm.getTimer() % 20 < 10) {
                ironGolem.hurtTime = 20;
            } else {
                ironGolem.hurtTime = 0;
            }
            ironGolemRenderer.render(ironGolem, 0F, 1F, matrixStack, buffer, combinedLightIn);
            matrixStack.pop();
        }

        matrixStack.pop();
    }

}
