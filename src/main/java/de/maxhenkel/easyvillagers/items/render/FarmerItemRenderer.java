package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.FarmerRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.model.data.EmptyModelData;

public class FarmerItemRenderer extends ItemStackTileEntityRenderer {

    private FarmerRenderer renderer;
    private Minecraft minecraft;

    private CachedMap<ItemStack, FarmerTileentity> cachedMap;

    public FarmerItemRenderer() {
        cachedMap = new CachedMap<>(10_000L, ItemUtils.ITEM_COMPARATOR);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void func_239207_a_(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = new FarmerRenderer(TileEntityRendererDispatcher.instance);
        }

        BlockState farmerBlock = ModBlocks.FARMER.getDefaultState();
        BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
        dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getCutoutMipped()), farmerBlock, dispatcher.getModelForState(farmerBlock), 0, 0, 0, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

        CompoundNBT blockEntityTag = itemStack.getChildTag("BlockEntityTag");
        if (blockEntityTag == null) {
            return;
        }

        FarmerTileentity farmer = cachedMap.get(itemStack, () -> {
            FarmerTileentity farmerTileentity = new FarmerTileentity();
            farmerTileentity.setFakeWorld(minecraft.world);
            farmerTileentity.read(null, blockEntityTag);
            return farmerTileentity;
        });
        renderer.render(farmer, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
