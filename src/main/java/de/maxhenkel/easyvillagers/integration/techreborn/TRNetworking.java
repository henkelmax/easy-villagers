package de.maxhenkel.easyvillagers.integration.techreborn;

public class TRNetworking {
    @SuppressWarnings("unchecked")
    public static void initNetworking() {
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<?> originalSlotSave = net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.unregisterGlobalReceiver(reborncore.common.network.serverbound.SlotSavePayload.ID.id());
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(reborncore.common.network.serverbound.SlotSavePayload.ID, (payload, context) -> {
            context.player().server.execute(() -> {
                net.minecraft.world.level.block.entity.BlockEntity be = context.player().level().getBlockEntity(payload.pos());
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
            });
        });

        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<?> originalIoSave = net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.unregisterGlobalReceiver(reborncore.common.network.serverbound.IoSavePayload.ID.id());
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(reborncore.common.network.serverbound.IoSavePayload.ID, (payload, context) -> {
            context.player().server.execute(() -> {
                net.minecraft.world.level.block.entity.BlockEntity be = context.player().level().getBlockEntity(payload.pos());
                if (be instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible compatible) {
                    // Ignore
                } else if (originalIoSave != null) {
                    ((net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<reborncore.common.network.serverbound.IoSavePayload>) originalIoSave).receive(payload, context);
                }
            });
        });

        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<?> originalConfigSave = net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.unregisterGlobalReceiver(reborncore.common.network.serverbound.SlotConfigSavePayload.ID.id());
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(reborncore.common.network.serverbound.SlotConfigSavePayload.ID, (payload, context) -> {
            context.player().server.execute(() -> {
                net.minecraft.world.level.block.entity.BlockEntity be = context.player().level().getBlockEntity(payload.pos());
                if (be instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible compatible) {
                    // Ignore
                } else if (originalConfigSave != null) {
                    ((net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler<reborncore.common.network.serverbound.SlotConfigSavePayload>) originalConfigSave).receive(payload, context);
                }
            });
        });
    }
}
