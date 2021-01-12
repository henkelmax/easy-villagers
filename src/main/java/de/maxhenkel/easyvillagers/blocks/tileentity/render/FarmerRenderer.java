package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;

public class FarmerRenderer extends VillagerRendererBase<FarmerTileentity> {

    public FarmerRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(FarmerTileentity farmer, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        super.render(farmer, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.push();

        Direction direction = Direction.SOUTH;
        if (!farmer.isFakeWorld()) {
            direction = farmer.getBlockState().get(TraderBlock.FACING);
        }

        if (farmer.getVillagerEntity() != null) {
            matrixStack.push();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.translate(0D, 0D, -4D / 16D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.render(farmer.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.pop();
        }

        BlockState crop = farmer.getCrop();
        if (crop != null) {
            matrixStack.push();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.translate(0D, 0D, 2D / 16D);
            matrixStack.translate(-0.5D, 0D, -0.5D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            matrixStack.translate(0.5D / 0.45D - 0.5D, 0D, 0.5D / 0.45D - 0.5D);

            BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
            int color = minecraft.getBlockColors().getColor(crop, null, null, 0);
            dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderTypeLookup.getRenderType(crop)), crop, dispatcher.getModelForState(crop), RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            matrixStack.pop();
        }

        matrixStack.pop();
    }

}
