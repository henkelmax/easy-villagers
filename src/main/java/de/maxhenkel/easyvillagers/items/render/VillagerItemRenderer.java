package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.corelib.client.RendererProviders;
import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.item.ItemStack;

public class VillagerItemRenderer extends ItemRenderer {

    private VillagerRenderer renderer;

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack stack, MultiBufferSource source, int light, int overlay) {
        if (renderer == null) {
            renderer = new VillagerRenderer(RendererProviders.createEntityRendererContext());
        }
        renderer.render(ModItems.VILLAGER.getVillagerFast(minecraft.level, itemStack), 0F, 1F, stack, source, light);
    }


}
