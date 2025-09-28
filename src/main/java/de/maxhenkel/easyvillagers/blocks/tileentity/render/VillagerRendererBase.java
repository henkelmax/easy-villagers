package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import de.maxhenkel.easyvillagers.blocks.tileentity.FakeWorldTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.VillagerRenderer;

import java.lang.ref.WeakReference;

public abstract class VillagerRendererBase<T extends FakeWorldTileentity, S extends BlockEntityRenderState> extends BlockRendererBase<T, S> {

    public static final int DEFAULT_LIGHT = 0xF000F0;

    protected WeakReference<VillagerRenderer> villagerRendererCache = new WeakReference<>(null);

    public VillagerRendererBase(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    protected VillagerRenderer getVillagerRenderer() {
        VillagerRenderer villagerRenderer = villagerRendererCache.get();
        if (villagerRenderer == null) {
            villagerRenderer = new VillagerRenderer(createEntityRenderer());
            villagerRendererCache = new WeakReference<>(villagerRenderer);
        }
        return villagerRenderer;
    }

    public static int getLightOrDefault(FakeWorldTileentity blockEntity, BlockEntityRenderState state) {
        if (blockEntity.isFakeWorld()) {
            return DEFAULT_LIGHT;
        }
        return state.lightCoords;
    }

}
