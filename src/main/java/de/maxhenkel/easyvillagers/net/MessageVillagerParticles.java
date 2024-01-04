package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MessageVillagerParticles implements Message<MessageVillagerParticles> {

    public static ResourceLocation ID = new ResourceLocation(Main.MODID, "villager_particles");

    private BlockPos pos;

    public MessageVillagerParticles(BlockPos pos) {
        this.pos = pos;
    }

    public MessageVillagerParticles() {

    }

    @Override
    public PacketFlow getExecutingSide() {
        return PacketFlow.CLIENTBOUND;
    }

    @Override
    public void executeClientSide(PlayPayloadContext context) {
        spawnParticles();
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.getBlockEntity(pos) instanceof BreederTileentity breeder) {
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

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
