package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderer;
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

public class BreederItemRenderer extends ItemStackTileEntityRenderer {

    private BreederRenderer renderer;
    private Minecraft minecraft;

    private CachedMap<ItemStack, BreederTileentity> cachedMap;

    public BreederItemRenderer() {
        cachedMap = new CachedMap<>(10_000L, ItemUtils.ITEM_COMPARATOR);
        minecraft = Minecraft.getInstance();
    }


    @Override
    public void func_239207_a_(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = new BreederRenderer(TileEntityRendererDispatcher.instance);
        }

        BlockState breederBlock = ModBlocks.BREEDER.getDefaultState();
        BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
        dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getCutoutMipped()), breederBlock, dispatcher.getModelForState(breederBlock), 0, 0, 0, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

        CompoundNBT blockEntityTag = itemStack.getChildTag("BlockEntityTag");
        if (blockEntityTag == null) {
            return;
        }

        BreederTileentity breeder = cachedMap.get(itemStack, () -> {
            BreederTileentity b = new BreederTileentity();
            b.setFakeWorld(minecraft.world);
            b.read(null, blockEntityTag);
            return b;
        });
        renderer.render(breeder, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
