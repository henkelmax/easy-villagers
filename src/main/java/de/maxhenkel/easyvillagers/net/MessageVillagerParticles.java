package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
        BlockEntity tileEntity = Minecraft.getInstance().level.getBlockEntity(pos);
        if (tileEntity instanceof BreederTileentity) {
            BreederTileentity breeder = (BreederTileentity) tileEntity;
            breeder.spawnParticles();
        }
    }

    @Override
    public MessageVillagerParticles fromBytes(FriendlyByteBuf packetBuffer) {
        pos = packetBuffer.readBlockPos();
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(pos);
    }
}
