package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MessageSelectTrade(boolean next) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageSelectTrade> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "select_trade"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSelectTrade> CODEC = StreamCodec.of(
        (buf, msg) -> buf.writeBoolean(msg.next()),
        buf -> new MessageSelectTrade(buf.readBoolean())
    );
    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
