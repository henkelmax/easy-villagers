package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.entity.EntityUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;

public class SyncableTileentity extends TileEntity {

    public SyncableTileentity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void sync() {
        if (world instanceof ServerWorld) {
            EntityUtils.forEachPlayerAround((ServerWorld) world, getPos(), 128D, this::syncContents);
        }
    }

    public void syncContents(ServerPlayerEntity player) {
        player.connection.sendPacket(getUpdatePacket());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        func_230337_a_(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

}
