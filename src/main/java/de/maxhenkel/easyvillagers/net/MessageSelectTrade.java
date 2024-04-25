package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.gui.AutoTraderContainer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageSelectTrade implements Message<MessageSelectTrade> {

    public static final CustomPacketPayload.Type<MessageSelectTrade> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(Main.MODID, "select_trade"));

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
    public void executeServerSide(IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer sender)) {
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
    public MessageSelectTrade fromBytes(RegistryFriendlyByteBuf packetBuffer) {
        next = packetBuffer.readBoolean();
        return this;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf packetBuffer) {
        packetBuffer.writeBoolean(next);
    }

    @Override
    public Type<MessageSelectTrade> type() {
        return TYPE;
    }

}
