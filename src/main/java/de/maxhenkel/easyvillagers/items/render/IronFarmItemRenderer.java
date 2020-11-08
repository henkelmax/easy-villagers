package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.IronFarmTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.IronFarmRenderer;
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

public class IronFarmItemRenderer extends ItemStackTileEntityRenderer {

    private IronFarmRenderer renderer;
    private Minecraft minecraft;

    private CachedMap<ItemStack, IronFarmTileentity> cachedMap;

    public IronFarmItemRenderer() {
        cachedMap = new CachedMap<>(10_000L);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void func_239207_a_(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = new IronFarmRenderer(TileEntityRendererDispatcher.instance);
        }

        BlockState farmerBlock = ModBlocks.IRON_FARM.getDefaultState();
        BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
        dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getCutoutMipped()), farmerBlock, dispatcher.getModelForState(farmerBlock), 0, 0, 0, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

        CompoundNBT blockEntityTag = itemStack.getChildTag("BlockEntityTag");
        if (blockEntityTag == null) {
            return;
        }

        IronFarmTileentity farm = cachedMap.get(itemStack, () -> {
            IronFarmTileentity farmTileentity = new IronFarmTileentity();
            farmTileentity.setFakeWorld(minecraft.world);
            farmTileentity.read(null, blockEntityTag);
            return farmTileentity;
        });
        renderer.render(farm, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
