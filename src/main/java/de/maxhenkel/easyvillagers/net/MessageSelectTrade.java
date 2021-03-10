package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.gui.AutoTraderContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.network.NetworkEvent;

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
        ServerPlayerEntity player = context.getSender();
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
    public MessageSelectTrade fromBytes(PacketBuffer packetBuffer) {
        next = packetBuffer.readBoolean();
        return this;
    }

    @Override
    public void toBytes(PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(next);
    }
}
