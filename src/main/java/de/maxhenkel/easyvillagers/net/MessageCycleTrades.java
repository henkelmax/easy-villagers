package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.events.TradeCycleEvents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageCycleTrades implements Message<MessageCycleTrades> {

    public static final CustomPacketPayload.Type<MessageCycleTrades> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "cycle_trades"));

    public MessageCycleTrades() {

    }

    @Override
    public PacketFlow getExecutingSide() {
        return PacketFlow.SERVERBOUND;
    }

    @Override
    public void executeServerSide(IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer sender)) {
            return;
        }
        TradeCycleEvents.onCycleTrades(sender);
    }

    @Override
    public MessageCycleTrades fromBytes(RegistryFriendlyByteBuf packetBuffer) {
        return this;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf packetBuffer) {
    }

    @Override
    public Type<MessageCycleTrades> type() {
        return TYPE;
    }

}
