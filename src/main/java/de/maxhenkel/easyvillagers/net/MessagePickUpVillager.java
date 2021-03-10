package de.maxhenkel.easyvillagers.net;

import de.maxhenkel.corelib.net.Message;
import de.maxhenkel.easyvillagers.events.VillagerEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.network.NetworkEvent;

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
        ServerPlayerEntity player = context.getSender();
        player.level.getEntitiesOfClass(VillagerEntity.class, player.getBoundingBox().inflate(8D), v -> v.getUUID().equals(villager)).stream().filter(LivingEntity::isAlive).findAny().ifPresent(villagerEntity -> {
            VillagerEvents.pickUp(villagerEntity, player);
        });
    }

    @Override
    public MessagePickUpVillager fromBytes(PacketBuffer packetBuffer) {
        villager = packetBuffer.readUUID();
        return this;
    }

    @Override
    public void toBytes(PacketBuffer packetBuffer) {
        packetBuffer.writeUUID(villager);
    }
}
