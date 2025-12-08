package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.events.VillagerEvents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.villager.Villager;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class MessagePickUpVillager implements Message<MessagePickUpVillager> {

    public static final CustomPacketPayload.Type<MessagePickUpVillager> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "pick_up_villager"));

    private UUID villager;

    public MessagePickUpVillager(UUID villager) {
        this.villager = villager;
    }

    public MessagePickUpVillager() {

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
        sender.level().getEntitiesOfClass(Villager.class, sender.getBoundingBox().inflate(8D), v -> v.getUUID().equals(villager)).stream().filter(VillagerEvents::arePickupConditionsMet).findAny().ifPresent(villagerEntity -> {
            VillagerEvents.pickUp(villagerEntity, sender);
        });
    }

    @Override
    public MessagePickUpVillager fromBytes(RegistryFriendlyByteBuf packetBuffer) {
        villager = packetBuffer.readUUID();
        return this;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf packetBuffer) {
        packetBuffer.writeUUID(villager);
    }

    @Override
    public Type<MessagePickUpVillager> type() {
        return TYPE;
    }

}
