package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageVillagerParticles implements Message<MessageVillagerParticles> {

    private BlockPos pos;

    public MessageVillagerParticles(BlockPos pos) {
        this.pos = pos;
    }

    public MessageVillagerParticles() {

    }

    @Override
    public Dist getExecutingSide() {
        return Dist.CLIENT;
    }

    @Override
    public void executeClientSide(NetworkEvent.Context context) { //TODO check server crash
        TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(pos);
        if (tileEntity instanceof BreederTileentity) {
            BreederTileentity breeder = (BreederTileentity) tileEntity;
            breeder.spawnParticles();
        }
    }

    @Override
    public MessageVillagerParticles fromBytes(PacketBuffer packetBuffer) {
        pos = packetBuffer.readBlockPos();
        return this;
    }

    @Override
    public void toBytes(PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(pos);
    }
}
