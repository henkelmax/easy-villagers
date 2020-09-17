package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderer;
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

public class TraderItemRenderer extends ItemStackTileEntityRenderer {

    private TraderRenderer renderer;
    private Minecraft minecraft;

    private CachedMap<ItemStack, TraderTileentity> cachedMap;

    public TraderItemRenderer() {
        cachedMap = new CachedMap<>(10_000L);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = new TraderRenderer(TileEntityRendererDispatcher.instance);
        }

        BlockState traderBlock = ModBlocks.TRADER.getDefaultState();
        BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
        dispatcher.getBlockModelRenderer().renderModel(matrixStack.peek(), buffer.getBuffer(RenderType.getCutoutMipped()), traderBlock, dispatcher.getModelForState(traderBlock), 0, 0, 0, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

        CompoundNBT blockEntityTag = itemStack.getChildTag("BlockEntityTag");
        if (blockEntityTag == null) {
            return;
        }

        TraderTileentity trader = cachedMap.get(itemStack, () -> {
            TraderTileentity traderTileentity = new TraderTileentity();
            traderTileentity.setFakeWorld(minecraft.world);
            traderTileentity.fromTag(null, blockEntityTag);
            return traderTileentity;
        });
        renderer.render(trader, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
