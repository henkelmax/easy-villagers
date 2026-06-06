package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MessageCycleTrades() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageCycleTrades> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "cycle_trades"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageCycleTrades> CODEC = StreamCodec.unit(new MessageCycleTrades());
    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
