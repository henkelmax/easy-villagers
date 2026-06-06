package de.maxhenkel.easyvillagers.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import de.maxhenkel.easyvillagers.events.TradeCycleEvents;
import de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import de.maxhenkel.easyvillagers.integration.techreborn.MessageSyncTRConfig;

public class Networking {
    @SuppressWarnings("unchecked")
    public static void init() {
        PayloadTypeRegistry.serverboundPlay().register(MessageCycleTrades.TYPE, MessageCycleTrades.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(MessageSelectTrade.TYPE, MessageSelectTrade.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(MessagePickUpVillager.TYPE, MessagePickUpVillager.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(MessageSyncTRConfig.ID, MessageSyncTRConfig.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(MessageVillagerParticles.TYPE, MessageVillagerParticles.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MessageCycleTrades.TYPE, (payload, context) -> {
            context.player().server.execute(() -> {
                TradeCycleEvents.onCycleTrades(context.player());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(MessagePickUpVillager.TYPE, (payload, context) -> {
            context.player().server.execute(() -> {
                Entity entity = context.player().level().getEntity(payload.villager());
                if (entity instanceof Villager villager) {
                    de.maxhenkel.easyvillagers.events.VillagerEvents.pickUp(villager, context.player());
                }
            });
        });
        
        ServerPlayNetworking.registerGlobalReceiver(MessageSelectTrade.TYPE, (payload, context) -> {
            context.player().server.execute(() -> {
                if (context.player().containerMenu instanceof de.maxhenkel.easyvillagers.gui.AutoTraderContainer autoTraderContainer) {
                    if (payload.next()) {
                        autoTraderContainer.getTrader().nextTrade();
                    } else {
                        autoTraderContainer.getTrader().prevTrade();
                    }
                }
            });
        });

        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
            net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTING.register(server -> {
                de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.initNetworking();
            });
        }
    }
}
