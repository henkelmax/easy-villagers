package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.core.Direction;

public class VillagerRenderStateBase extends BlockEntityRenderState {

    public Direction direction;
    public boolean renderVillager;
    public VillagerRenderState villagerRenderState = new VillagerRenderState();

    public void apply(VillagerTileentity blockEntity) {
        direction = Direction.SOUTH;
        if (!blockEntity.isFakeWorld()) {
            direction = blockEntity.getBlockState().getValue(TraderBlock.FACING);
        }
    }

}
