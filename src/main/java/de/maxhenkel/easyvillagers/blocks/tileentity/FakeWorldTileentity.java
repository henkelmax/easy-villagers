package de.maxhenkel.easyvillagers.blocks.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FakeWorldTileentity extends SyncableTileentity {

    private boolean fakeWorld;
    private BlockState defaultState;

    public FakeWorldTileentity(BlockEntityType<?> type, BlockState defaultState, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.defaultState = defaultState;
    }

    public void setFakeWorld(Level w) {
        level = w;
        fakeWorld = true;
    }

    public boolean isFakeWorld() {
        return fakeWorld;
    }

    @Override
    public BlockState getBlockState() {
        if (fakeWorld) {
            return defaultState;
        }
        return super.getBlockState();
    }

}
