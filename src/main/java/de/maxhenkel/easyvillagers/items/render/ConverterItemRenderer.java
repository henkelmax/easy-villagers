package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.ConverterRenderer;
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

public class ConverterItemRenderer extends ItemStackTileEntityRenderer {

    private ConverterRenderer renderer;
    private Minecraft minecraft;

    private CachedMap<ItemStack, ConverterTileentity> cachedMap;

    public ConverterItemRenderer() {
        cachedMap = new CachedMap<>(10_000L);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void func_239207_a_(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = new ConverterRenderer(TileEntityRendererDispatcher.instance);
        }

        BlockState breederBlock = ModBlocks.CONVERTER.getDefaultState();
        BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
        dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getCutoutMipped()), breederBlock, dispatcher.getModelForState(breederBlock), 0, 0, 0, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

        CompoundNBT blockEntityTag = itemStack.getChildTag("BlockEntityTag");
        if (blockEntityTag == null) {
            return;
        }

        ConverterTileentity converter = cachedMap.get(itemStack, () -> {
            ConverterTileentity b = new ConverterTileentity();
            b.setFakeWorld(minecraft.world);
            b.func_230337_a_(null, blockEntityTag);
            return b;
        });
        renderer.render(converter, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
