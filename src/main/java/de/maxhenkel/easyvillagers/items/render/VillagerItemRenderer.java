package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;

public class VillagerItemRenderer extends ItemStackTileEntityRenderer {

    private Minecraft minecraft;
    private VillagerRenderer renderer;

    public VillagerItemRenderer() {
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (renderer == null) {
            renderer = new VillagerRenderer(minecraft.getEntityRenderDispatcher(), (IReloadableResourceManager) minecraft.getResourceManager());
        }
        renderer.render(ModItems.VILLAGER.getVillagerFast(minecraft.level, itemStackIn), 0F, 1F, matrixStackIn, bufferIn, combinedLightIn);
    }

}
