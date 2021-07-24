package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.events.VillagerEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.UUID;

public class MessagePickUpVillager implements Message<MessagePickUpVillager> {

    private UUID villager;

    public MessagePickUpVillager(UUID villager) {
        this.villager = villager;
    }

    public MessagePickUpVillager() {

    }

    @Override
    public Dist getExecutingSide() {
        return Dist.DEDICATED_SERVER;
    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        player.level.getEntitiesOfClass(Villager.class, player.getBoundingBox().inflate(8D), v -> v.getUUID().equals(villager)).stream().filter(LivingEntity::isAlive).findAny().ifPresent(villagerEntity -> {
            VillagerEvents.pickUp(villagerEntity, player);
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
}
