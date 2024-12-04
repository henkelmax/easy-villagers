package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.world.entity.npc.Villager;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

public class VillagerRendererBase<T extends FakeWorldTileentity> extends BlockRendererBase<T> {

    protected WeakReference<VillagerRenderer> villagerRendererCache = new WeakReference<>(null);

    @Nullable
    protected static VillagerRenderState villagerRenderState;

    public VillagerRendererBase(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public void render(T tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    protected VillagerRenderer getVillagerRenderer() {
        VillagerRenderer villagerRenderer = villagerRendererCache.get();
        if (villagerRenderer == null) {
            villagerRenderer = new VillagerRenderer(createEntityRenderer());
            villagerRendererCache = new WeakReference<>(villagerRenderer);
        }
        return villagerRenderer;
    }

    protected static VillagerRenderState getVillagerRenderState(VillagerRenderer renderer, Villager villager) {
        if (villagerRenderState == null) {
            villagerRenderState = renderer.createRenderState();
        }
        renderer.extractRenderState(villager, villagerRenderState, 0F);
        return villagerRenderState;
    }

}
