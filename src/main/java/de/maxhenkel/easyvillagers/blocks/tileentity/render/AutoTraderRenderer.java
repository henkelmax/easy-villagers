package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

public class AutoTraderRenderer extends VillagerRendererBase<AutoTraderTileentity> {

    public AutoTraderRenderer(EntityModelSet entityModelSet) {
        super(entityModelSet);
    }

    @Override
    public void render(AutoTraderTileentity trader, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, Vec3 vec) {
        super.render(trader, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay, vec);
        TraderRenderer.renderTraderBase(getVillagerRenderer(), trader, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

}
