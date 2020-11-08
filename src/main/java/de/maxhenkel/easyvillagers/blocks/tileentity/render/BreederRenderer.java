package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.tileentity.BedTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class BreederRenderer extends TileEntityRenderer<BreederTileentity> {

    private Minecraft minecraft;
    private VillagerRenderer renderer;
    private BedTileEntityRenderer bedRenderer;
    private BedTileEntity bed;

    public BreederRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(BreederTileentity breeder, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.push();

        if (renderer == null) {
            renderer = new VillagerRenderer(minecraft.getRenderManager(), (IReloadableResourceManager) minecraft.getResourceManager());
        }

        if (bedRenderer == null) {
            bedRenderer = new BedTileEntityRenderer(TileEntityRendererDispatcher.instance);
            bed = new BedTileEntity(DyeColor.RED);
        }

        Direction direction = Direction.SOUTH;
        if (!breeder.isFakeWorld()) {
            direction = breeder.getBlockState().get(TraderBlock.FACING);
        }

        if (breeder.getVillagerEntity1() != null) {
            matrixStack.push();
            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.translate(-5D / 16D, 0D, 0D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            renderer.render(breeder.getVillagerEntity1(), 0F, 1F, matrixStack, buffer, combinedLightIn);
            matrixStack.pop();
        }

        if (breeder.getVillagerEntity2() != null) {
            matrixStack.push();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.translate(5D / 16D, 0D, 0D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-90));
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            renderer.render(breeder.getVillagerEntity2(), 0F, 1F, matrixStack, buffer, combinedLightIn);
            matrixStack.pop();
        }

        matrixStack.push();
        matrixStack.translate(0.5D, 1D / 16D, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
        matrixStack.translate(0D, 0D, 3D / 16D);
        matrixStack.translate(-0.5D, 0D, -0.5D);
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        matrixStack.translate(0.5D / 0.4D - 0.5D, 0D, 0.5D / 0.4D - 0.5D);
        bedRenderer.render(bed, 1F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
        matrixStack.pop();

        matrixStack.pop();
    }

}
