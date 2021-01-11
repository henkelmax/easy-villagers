package de.maxhenkel.easyvillagers.blocks.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public class FakeWorldTileentity extends SyncableTileentity {

    private boolean fakeWorld;
    private BlockState defaultState;

    public FakeWorldTileentity(TileEntityType<?> tileEntityType, BlockState defaultState) {
        super(tileEntityType);
        this.defaultState = defaultState;
    }

    public void setFakeWorld(World w) {
        world = w;
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
