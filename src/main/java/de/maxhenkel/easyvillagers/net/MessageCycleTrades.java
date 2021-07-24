package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.events.GuiEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class MessageCycleTrades implements Message<MessageCycleTrades> {

    public MessageCycleTrades() {

    }

    @Override
    public Dist getExecutingSide() {
        return Dist.DEDICATED_SERVER;
    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {
        GuiEvents.onCycleTrades(context.getSender());
    }

    @Override
    public MessageCycleTrades fromBytes(FriendlyByteBuf packetBuffer) {
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf packetBuffer) {
    }
}
