package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.events.GuiEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MessageCycleTrades implements Message<MessageCycleTrades> {

    public static ResourceLocation ID = new ResourceLocation(Main.MODID, "cycle_trades");

    public MessageCycleTrades() {

    }

    @Override
    public PacketFlow getExecutingSide() {
        return PacketFlow.SERVERBOUND;
    }

    @Override
    public void executeServerSide(PlayPayloadContext context) {
        if (!(context.player().orElse(null) instanceof ServerPlayer sender)) {
            return;
        }
        GuiEvents.onCycleTrades(sender);
    }

    @Override
    public MessageCycleTrades fromBytes(FriendlyByteBuf packetBuffer) {
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf packetBuffer) {
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
