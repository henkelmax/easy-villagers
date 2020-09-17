package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.ZombieVillagerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class ConverterRenderer extends TileEntityRenderer<ConverterTileentity> {

    private Minecraft minecraft;
    private VillagerRenderer renderer;
    private ZombieEntity zombie;
    private ZombieRenderer zombieRenderer;
    private ZombieVillagerRenderer zombieVillagerRenderer;
    private ZombieVillagerEntity zombieVillager;

    public ConverterRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(ConverterTileentity converter, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.push();

        if (renderer == null) {
            renderer = new VillagerRenderer(minecraft.getRenderManager(), (IReloadableResourceManager) minecraft.getResourceManager());
        }

        if (zombieRenderer == null) {
            zombieRenderer = new ZombieRenderer(minecraft.getRenderManager());
            zombie = new ZombieEntity(minecraft.world);
        }

        if (zombieVillagerRenderer == null) {
            zombieVillagerRenderer = new ZombieVillagerRenderer(minecraft.getRenderManager(), (IReloadableResourceManager) minecraft.getResourceManager());
            zombieVillager = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, minecraft.world);
        }

        Direction direction = Direction.SOUTH;
        if (!converter.isFakeWorld()) {
            direction = converter.getBlockState().get(TraderBlock.FACING);
        }
        VillagerEntity villagerEntity = converter.getVillagerEntity();
        if (villagerEntity != null) {
            matrixStack.push();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-direction.getHorizontalAngle()));
            matrixStack.translate(-5D / 16D, 0D, 0D);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
            matrixStack.scale(0.4F, 0.4F, 0.4F);
            if (converter.getTimer() >= ConverterTileentity.getZombifyTime() && converter.getTimer() < ConverterTileentity.getConvertTime()) {
                zombieVillager.func_213792_a(villagerEntity.getVillagerData());
                zombieVillager.setChild(villagerEntity.isChild());
                zombieVillagerRenderer.render(zombieVillager, 0F, 1F, matrixStack, buffer, combinedLightIn);
            } else {
                renderer.render(villagerEntity, 0F, 1F, matrixStack, buffer, combinedLightIn);
            }
            matrixStack.pop();
        }

        matrixStack.push();

        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-direction.getHorizontalAngle()));
        matrixStack.translate(5D / 16D, 0D, 0D);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90));
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        zombieRenderer.render(zombie, 0F, 1F, matrixStack, buffer, combinedLightIn);
        matrixStack.pop();

        matrixStack.pop();
    }

}
