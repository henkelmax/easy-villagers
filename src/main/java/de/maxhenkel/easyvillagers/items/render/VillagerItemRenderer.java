package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.corelib.client.RendererProviders;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class VillagerItemRenderer extends ItemRenderer {

    private VillagerRenderer renderer;

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack stack, MultiBufferSource source, int light, int overlay) {
        if (renderer == null) {
            renderer = new VillagerRenderer(RendererProviders.createEntityRendererContext());
        }
        EasyVillagerEntity cacheVillager = VillagerData.getCacheVillager(itemStack, minecraft.level);
        renderer.render(cacheVillager, 0F, 1F, stack, source, light);
    }

}
