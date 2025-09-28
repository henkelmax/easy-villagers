package de.maxhenkel.easyvillagers.items.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BlockRendererBase;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.VillagerRendererBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Set;

public class VillagerSpecialRenderer implements SpecialModelRenderer<VillagerRenderState> {

    protected static final Minecraft minecraft = Minecraft.getInstance();

    @Nullable
    private VillagerRenderer renderer;
    private final CameraRenderState cameraRenderState;

    public VillagerSpecialRenderer(EntityModelSet modelSet) {
        cameraRenderState = new CameraRenderState();
    }

    @Override
    public void submit(@Nullable VillagerRenderState state, ItemDisplayContext context, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean b) {
        if (state == null) {
            return;
        }
        getRenderer().submit(state, stack, collector, cameraRenderState);
    }

    @Override
    public void getExtents(Set<Vector3f> vecs) {

    }

    @Nullable
    @Override
    public VillagerRenderState extractArgument(ItemStack stack) {
        EasyVillagerEntity cacheVillager = VillagerData.getCacheVillager(stack, minecraft.level);
        VillagerRenderState renderState = getRenderer().createRenderState();
        getRenderer().extractRenderState(cacheVillager, renderState, 0F);
        renderState.lightCoords = VillagerRendererBase.DEFAULT_LIGHT;
        return renderState;
    }

    private VillagerRenderer getRenderer() {
        if (renderer == null) {
            renderer = new VillagerRenderer(BlockRendererBase.createEntityRenderer());
        }
        return renderer;
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<VillagerSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(VillagerSpecialRenderer.Unbaked::new);

        public Unbaked() {

        }

        @Override
        @Nullable
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new VillagerSpecialRenderer(context.entityModelSet());
        }

        @Override
        public MapCodec<VillagerSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

    }

}

