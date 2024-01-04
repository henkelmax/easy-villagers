package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.gui.AutoTraderContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MessageSelectTrade implements Message<MessageSelectTrade> {

    public static ResourceLocation ID = new ResourceLocation(Main.MODID, "select_trade");

    private boolean next;

    public MessageSelectTrade(boolean next) {
        this.next = next;
    }

    public MessageSelectTrade() {

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
        if (sender.containerMenu instanceof AutoTraderContainer container) {
            if (next) {
                container.getTrader().nextTrade();
            } else {
                container.getTrader().prevTrade();
            }
        }
    }

    @Override
    public MessageSelectTrade fromBytes(FriendlyByteBuf packetBuffer) {
        next = packetBuffer.readBoolean();
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBoolean(next);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
