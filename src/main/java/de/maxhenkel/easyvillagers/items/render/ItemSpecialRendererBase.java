package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BlockRendererBase;
import de.maxhenkel.easyvillagers.items.BlockItemDataCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;
import java.util.function.Supplier;

public class ItemSpecialRendererBase<T extends FakeWorldTileentity, U extends BlockEntityRenderState> implements SpecialModelRenderer<T> {

    protected static final Minecraft minecraft = Minecraft.getInstance();

    protected BlockRendererBase<T, U> renderer;
    protected Supplier<BlockState> blockSupplier;
    protected Class<T> typeClass;
    @Nullable
    protected U renderState;
    protected CameraRenderState cameraRenderState;

    public ItemSpecialRendererBase(Supplier<BlockState> blockSupplier, Class<T> typeClass) {
        this.blockSupplier = blockSupplier;
        this.typeClass = typeClass;
        this.cameraRenderState = new CameraRenderState();
        cameraRenderState.initialized = true;
    }

    @Override
    public void submit(@Nullable T blockEntity, ItemDisplayContext context, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean b, int i) {
        collector.submitBlock(stack, blockSupplier.get(), light, overlay, 0);
        if (blockEntity == null) {
            return;
        }
        if (renderState == null) {
            renderState = renderer.createRenderState();
        }
        renderState.lightCoords = light;
        renderer.extractRenderState(blockEntity, renderState, 0F, Vec3.ZERO, null);
        renderState.lightCoords = light;
        renderer.submit(renderState, stack, collector, cameraRenderState);
    }

    @Override
    public void getExtents(Set<Vector3f> vecs) {

    }

    @Nullable
    @Override
    public T extractArgument(ItemStack stack) {
        return BlockItemDataCache.get(minecraft.level, stack, typeClass);
    }
}

