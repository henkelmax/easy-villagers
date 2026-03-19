package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import net.minecraft.client.renderer.blockentity.state.BedRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.BedPart;

public class BreederRenderState extends BlockEntityRenderState {

    public Direction direction;
    public boolean renderVillager1;
    public VillagerRenderState villagerRenderState1 = new VillagerRenderState();
    public boolean renderVillager2;
    public VillagerRenderState villagerRenderState2 = new VillagerRenderState();
    public BedRenderState bedRenderStateBottom = new BedRenderState();
    public BedRenderState bedRenderStateTop = new BedRenderState();

    public BreederRenderState() {
        bedRenderStateBottom.color = DyeColor.RED;
        bedRenderStateBottom.part = BedPart.FOOT;
        bedRenderStateTop.color = DyeColor.RED;
        bedRenderStateTop.part = BedPart.HEAD;
    }

}
