package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import java.util.UUID;

public record MessagePickUpVillager(UUID villager) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessagePickUpVillager> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "pick_up_villager"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessagePickUpVillager> CODEC = StreamCodec.of(
        (buf, msg) -> buf.writeUUID(msg.villager()),
        buf -> new MessagePickUpVillager(buf.readUUID())
    );
    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
