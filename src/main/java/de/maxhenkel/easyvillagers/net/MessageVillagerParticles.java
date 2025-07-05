package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageVillagerParticles implements Message<MessageVillagerParticles> {

    public static final CustomPacketPayload.Type<MessageVillagerParticles> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "villager_particles"));

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
    public void executeClientSide(IPayloadContext context) {
        spawnParticles();
    }

    private void spawnParticles() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.getBlockEntity(pos) instanceof BreederTileentity breeder) {
            breeder.spawnParticles();
        }
    }

    @Override
    public MessageVillagerParticles fromBytes(RegistryFriendlyByteBuf packetBuffer) {
        pos = packetBuffer.readBlockPos();
        return this;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(pos);
    }

    @Override
    public Type<MessageVillagerParticles> type() {
        return TYPE;
    }

}
