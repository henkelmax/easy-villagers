package de.maxhenkel.easyvillagers.blocks.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public class FakeWorldTileentity extends SyncableTileentity {

    private boolean fakeWorld;

    public FakeWorldTileentity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void setFakeWorld(World w) {
        world = w;
        fakeWorld = true;
    }

    public boolean isFakeWorld() {
        return fakeWorld;
    }

}
