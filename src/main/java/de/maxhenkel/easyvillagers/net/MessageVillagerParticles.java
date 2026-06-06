package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

public record MessageVillagerParticles(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageVillagerParticles> TYPE = new CustomPacketPayload.Type<>(Identifier.withDefaultNamespace("easy_villagers_villager_particles"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageVillagerParticles> CODEC = StreamCodec.of(
        (buf, msg) -> buf.writeBlockPos(msg.pos()),
        buf -> new MessageVillagerParticles(buf.readBlockPos())
    );
    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
