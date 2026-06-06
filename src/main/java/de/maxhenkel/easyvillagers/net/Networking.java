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

                net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<?> originalSlotSave = net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.unregisterGlobalReceiver(reborncore.common.network.serverbound.SlotSavePayload.ID.id());
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(reborncore.common.network.serverbound.SlotSavePayload.ID, (payload, context) -> {
                context.player().server.execute(() -> {
                    BlockEntity be = context.player().level().getBlockEntity(payload.pos());
                    if (be instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible compatible) {
                        Object configObj = compatible.getSlotConfiguration();
                        if (configObj instanceof de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration config) {
                            config.setConfig(payload.slotConfig().getSlotID(), payload.slotConfig().getSide(), de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration.ExtractConfig.values()[payload.slotConfig().getSlotIO().getIoConfig().ordinal()]);
                            be.setChanged();
                                                        if (be instanceof de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity vbe) {
                                vbe.sync();
                            } else if (be instanceof de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity br) {
                                br.sync();
                            } else if (be instanceof de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity inc) {
                                inc.sync();
                            } else if (be instanceof de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity conv) {
                                conv.sync();
                            }
                        }
                    } else if (originalSlotSave != null) {
                        ((net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<reborncore.common.network.serverbound.SlotSavePayload>) originalSlotSave).receive(payload, context);
                    }
                }); });             net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<?> originalIoSave = net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.unregisterGlobalReceiver(reborncore.common.network.serverbound.IoSavePayload.ID.id());
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(reborncore.common.network.serverbound.IoSavePayload.ID, (payload, context) -> {
                context.player().server.execute(() -> {
                    BlockEntity be = context.player().level().getBlockEntity(payload.pos());
                    if (be instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible compatible) {
                        // EasyVillagers does not need complex Io configs. Ignore.
                    } else if (originalIoSave != null) {
                        ((net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<reborncore.common.network.serverbound.IoSavePayload>) originalIoSave).receive(payload, context);
                    }
                }); });             net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<?> originalConfigSave = net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.unregisterGlobalReceiver(reborncore.common.network.serverbound.SlotConfigSavePayload.ID.id());
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(reborncore.common.network.serverbound.SlotConfigSavePayload.ID, (payload, context) -> {
                context.player().server.execute(() -> {
                    BlockEntity be = context.player().level().getBlockEntity(payload.pos());
                    if (be instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible compatible) {
                        // EasyVillagers does not need clipboard paste for config. Ignore.
                    } else if (originalConfigSave != null) {
                        ((net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<reborncore.common.network.serverbound.SlotConfigSavePayload>) originalConfigSave).receive(payload, context);
                    }
                });
            }); });
    }
}
