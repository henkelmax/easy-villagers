package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;

public class VillagerRendererBase<T extends FakeWorldTileentity> extends BlockRendererBase<T> {

    protected VillagerRenderer villagerRenderer;

    public VillagerRendererBase(BlockEntityRendererProvider.Context renderer) {
        super(renderer);
    }

    @Override
    public void render(T tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (villagerRenderer == null) {
            villagerRenderer = new VillagerRenderer(getEntityRenderer());
        }
        super.render(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

}
