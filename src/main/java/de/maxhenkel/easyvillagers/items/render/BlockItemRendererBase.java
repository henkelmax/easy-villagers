package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.corelib.client.RendererProviders;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import de.maxhenkel.easyvillagers.datacomponents.VillagerBlockEntityData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
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
    public void renderByItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = rendererSupplier.apply(RendererProviders.createBlockEntityRendererContext());
        }
        if (itemStack.getItem() instanceof BlockItem blockItem) {
            minecraft.getBlockRenderer().renderSingleBlock(blockItem.getBlock().defaultBlockState(), matrixStack, buffer, combinedLightIn, combinedOverlayIn);
        }
        U be = VillagerBlockEntityData.getAndStoreBlockEntity(itemStack, minecraft.level.registryAccess(), minecraft.level, tileEntitySupplier);
        renderer.render(be, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
