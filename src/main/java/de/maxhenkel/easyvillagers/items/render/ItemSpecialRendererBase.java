package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BlockRendererBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerBlockEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ItemSpecialRendererBase<T extends FakeWorldTileentity> implements SpecialModelRenderer<T> {

    protected static final Minecraft minecraft = Minecraft.getInstance();

    protected BlockRendererBase<T> renderer;
    protected Supplier<BlockState> blockSupplier;
    protected Supplier<T> blockEntitySupplier;

    public ItemSpecialRendererBase(EntityModelSet modelSet, Supplier<BlockState> blockSupplier, Supplier<T> blockEntitySupplier) {
        this.blockSupplier = blockSupplier;
        this.blockEntitySupplier = blockEntitySupplier;
    }

    @Override
    public void render(@Nullable T blockEntity, ItemDisplayContext itemDisplayContext, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay, boolean b) {
        minecraft.getBlockRenderer().renderSingleBlock(blockSupplier.get(), stack, bufferSource, light, overlay);
        if (blockEntity == null) {
            return;
        }
        renderer.render(blockEntity, 0F, stack, bufferSource, light, overlay, Vec3.ZERO);
    }

    @Nullable
    @Override
    public T extractArgument(ItemStack stack) {
        return VillagerBlockEntityData.getAndStoreBlockEntity(stack, minecraft.level.registryAccess(), minecraft.level, blockEntitySupplier);
    }
}

