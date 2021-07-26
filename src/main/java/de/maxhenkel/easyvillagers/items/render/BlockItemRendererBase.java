package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.corelib.client.RendererProviders;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockItemRendererBase<T extends BlockEntityRenderer<U>, U extends FakeWorldTileentity> extends ItemRenderer {

    private Function<BlockEntityRendererProvider.Context, T> rendererSupplier;
    private Supplier<U> tileEntitySupplier;
    private T renderer;
    private Minecraft minecraft;

    private CachedMap<ItemStack, U> cachedMap;

    public BlockItemRendererBase(Function<BlockEntityRendererProvider.Context, T> rendererSupplier, Supplier<U> tileentitySupplier) {
        this.rendererSupplier = rendererSupplier;
        this.tileEntitySupplier = tileentitySupplier;
        cachedMap = new CachedMap<>(10_000L, ItemUtils.ITEM_COMPARATOR);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = rendererSupplier.apply(RendererProviders.createBlockEntityRendererContext());
        }

        CompoundTag blockEntityTag = itemStack.getTagElement("BlockEntityTag");
        U tileEntity = cachedMap.get(itemStack, () -> {
            U te = tileEntitySupplier.get();
            te.setFakeWorld(minecraft.level);
            if (blockEntityTag != null) {
                te.load(blockEntityTag);
            }
            return te;
        });
        renderer.render(tileEntity, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
