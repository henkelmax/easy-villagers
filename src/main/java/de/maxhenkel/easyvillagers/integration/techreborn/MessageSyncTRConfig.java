package de.maxhenkel.easyvillagers.integration.techreborn;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MessageSyncTRConfig(BlockPos pos, int slot, Direction side, int configMode) implements CustomPacketPayload {

    public static final Type<MessageSyncTRConfig> ID = new Type<>(Identifier.parse("easy_villagers:sync_tr_config"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSyncTRConfig> CODEC = StreamCodec.of(
            (buf, msg) -> msg.write(buf),
            MessageSyncTRConfig::read
    );

    public static MessageSyncTRConfig read(RegistryFriendlyByteBuf buf) {
        return new MessageSyncTRConfig(buf.readBlockPos(), buf.readInt(), Direction.from3DDataValue(buf.readInt()), buf.readInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(slot);
        buf.writeInt(side.get3DDataValue());
        buf.writeInt(configMode);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
