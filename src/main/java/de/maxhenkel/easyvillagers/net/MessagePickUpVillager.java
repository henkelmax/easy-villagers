package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.events.VillagerEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.UUID;

public class MessagePickUpVillager implements Message<MessagePickUpVillager> {

    public static ResourceLocation ID = new ResourceLocation(Main.MODID, "pick_up_villager");

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
    public void executeServerSide(PlayPayloadContext context) {
        if (!(context.player().orElse(null) instanceof ServerPlayer sender)) {
            return;
        }
        sender.level().getEntitiesOfClass(Villager.class, sender.getBoundingBox().inflate(8D), v -> v.getUUID().equals(villager)).stream().filter(VillagerEvents::arePickupConditionsMet).findAny().ifPresent(villagerEntity -> {
            VillagerEvents.pickUp(villagerEntity, sender);
        });
    }

    @Override
    public MessagePickUpVillager fromBytes(FriendlyByteBuf packetBuffer) {
        villager = packetBuffer.readUUID();
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUUID(villager);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
