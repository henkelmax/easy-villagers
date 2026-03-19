package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.BlockModelRenderState;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TraderRenderState extends VillagerRenderStateBase {

    public final BlockModelRenderState workstation = new BlockModelRenderState();
    @Nullable
    public Consumer<PoseStack> blockTransforms;
    public final BlockModelRenderState topBlock = new BlockModelRenderState();

}
