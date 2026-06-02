package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.core.Direction;

public class BreederRenderState extends BlockEntityRenderState {

    public Direction direction;
    public boolean renderVillager1;
    public VillagerRenderState villagerRenderState1 = new VillagerRenderState();
    public boolean renderVillager2;
    public VillagerRenderState villagerRenderState2 = new VillagerRenderState();
    public final BlockModelRenderState bedFoot = new BlockModelRenderState();
    public final BlockModelRenderState bedHead = new BlockModelRenderState();

}
