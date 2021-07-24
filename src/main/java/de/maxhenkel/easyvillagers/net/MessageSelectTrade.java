package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.gui.AutoTraderContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class MessageSelectTrade implements Message<MessageSelectTrade> {

    private boolean next;

    public MessageSelectTrade(boolean next) {
        this.next = next;
    }

    public MessageSelectTrade() {

    }

    @Override
    public Dist getExecutingSide() {
        return Dist.DEDICATED_SERVER;
    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player.containerMenu instanceof AutoTraderContainer) {
            AutoTraderContainer container = (AutoTraderContainer) player.containerMenu;
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
}
