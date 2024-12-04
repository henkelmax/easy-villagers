package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BlockRendererBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class VillagerSpecialRenderer implements SpecialModelRenderer<VillagerRenderState> {

    protected static final Minecraft minecraft = Minecraft.getInstance();

    private VillagerRenderer renderer;
    private VillagerRenderState renderState;

    public VillagerSpecialRenderer(EntityModelSet modelSet) {

    }

    @Override
    public void render(@Nullable VillagerRenderState villagerRenderState, ItemDisplayContext itemDisplayContext, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay, boolean b) {
        if (villagerRenderState == null) {
            return;
        }
        getRenderer().render(villagerRenderState, stack, bufferSource, light);
    }

    @Nullable
    @Override
    public VillagerRenderState extractArgument(ItemStack stack) {
        EasyVillagerEntity cacheVillager = VillagerData.getCacheVillager(stack, minecraft.level);
        if (renderState == null) {
            renderState = getRenderer().createRenderState();
        }
        renderer.extractRenderState(cacheVillager, renderState, 0F);
        return renderState;
    }

    private VillagerRenderer getRenderer() {
        if (renderer == null) {
            renderer = new VillagerRenderer(BlockRendererBase.createEntityRenderer());
        }
        return renderer;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<VillagerSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(VillagerSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        public MapCodec<VillagerSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new VillagerSpecialRenderer(modelSet);
        }
    }

}

