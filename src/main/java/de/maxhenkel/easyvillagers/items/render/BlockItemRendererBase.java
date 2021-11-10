package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.corelib.client.RendererProviders;
import de.maxhenkel.easyvillagers.ItemTileEntityCache;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockItemRendererBase<T extends BlockEntityRenderer<U>, U extends FakeWorldTileentity> extends ItemRenderer {

    private final Function<BlockEntityRendererProvider.Context, T> rendererSupplier;
    private final Supplier<U> tileEntitySupplier;
    private T renderer;

    public BlockItemRendererBase(Function<BlockEntityRendererProvider.Context, T> rendererSupplier, Supplier<U> tileentitySupplier) {
        this.rendererSupplier = rendererSupplier;
        this.tileEntitySupplier = tileentitySupplier;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = rendererSupplier.apply(RendererProviders.createBlockEntityRendererContext());
        }
        renderer.render(ItemTileEntityCache.getTileEntity(itemStack, tileEntitySupplier), 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
