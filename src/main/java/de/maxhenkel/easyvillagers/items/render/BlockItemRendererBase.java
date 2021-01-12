package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockItemRendererBase<T extends TileEntityRenderer<U>, U extends FakeWorldTileentity> extends ItemStackTileEntityRenderer {

    private Function<TileEntityRendererDispatcher, T> rendererSupplier;
    private Supplier<U> tileEntitySupplier;
    private T renderer;
    private Minecraft minecraft;

    private CachedMap<ItemStack, U> cachedMap;

    public BlockItemRendererBase(Function<TileEntityRendererDispatcher, T> rendererSupplier, Supplier<U> tileentitySupplier) {
        this.rendererSupplier = rendererSupplier;
        this.tileEntitySupplier = tileentitySupplier;
        cachedMap = new CachedMap<>(10_000L, ItemUtils.ITEM_COMPARATOR);
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(ItemStack itemStack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = rendererSupplier.apply(TileEntityRendererDispatcher.instance);
        }

        CompoundNBT blockEntityTag = itemStack.getChildTag("BlockEntityTag");
        U tileEntity = cachedMap.get(itemStack, () -> {
            U te = tileEntitySupplier.get();
            te.setFakeWorld(minecraft.world);
            if (blockEntityTag != null) {
                te.read(blockEntityTag);
            }
            return te;
        });
        renderer.render(tileEntity, 0F, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
    }

}
