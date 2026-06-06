package de.maxhenkel.easyvillagers.mixin.techreborn;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import reborncore.client.gui.config.SlotConfigGui;
import reborncore.client.gui.config.elements.SlotConfigPopupElement;
import reborncore.common.network.serverbound.IoSavePayload;
import reborncore.common.network.serverbound.SlotConfigSavePayload;
import reborncore.common.network.serverbound.SlotSavePayload;
import de.maxhenkel.easyvillagers.integration.techreborn.MessageSyncTRConfig;

@Mixin({SlotConfigPopupElement.class, SlotConfigGui.class})
public class TRClientPayloadMixin {

                @Redirect(method = {"cycleConfig", "updateCheckBox", "pasteFromClipboard"}, at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/api/client/networking/v1/ClientPlayNetworking;send(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V"), require = 0)
    private void easyvillagers$redirectSend(CustomPacketPayload payload) {
        if (payload instanceof SlotSavePayload slotSave) {
            net.minecraft.world.level.block.entity.BlockEntity be = net.minecraft.client.Minecraft.getInstance().level.getBlockEntity(slotSave.pos());
            if (be instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) {
                reborncore.common.blockentity.SlotConfiguration.SlotConfig config = slotSave.slotConfig();
                ClientPlayNetworking.send(payload);
                
                if (net.minecraft.client.Minecraft.getInstance().screen instanceof de.maxhenkel.easyvillagers.gui.ScreenBase<?> evScreen) {
                    if (evScreen.trSlotConfigGui != null) {
                        reborncore.client.gui.GuiBase<?> gui = ((reborncore.client.gui.config.SlotConfigGui)evScreen.trSlotConfigGui).gui();
                        if (gui != null && gui.getMachine() != null) {
                            gui.getMachine().getSlotConfiguration().getSlotDetails(config.getSlotID()).updateSlotConfig(config);
                        }
                    }
                }
                return;
            }
        }
        if (payload instanceof IoSavePayload ioSave) {
            // Easy Villagers does not support auto-input/output toggles (it always pipes).
            // We just ignore this packet for our blocks.
            // Wait, we need a way to tell if it's OUR block or TR's block!
            // If the block at pos() is our block, we drop it.
            net.minecraft.world.level.block.entity.BlockEntity be = net.minecraft.client.Minecraft.getInstance().level.getBlockEntity(ioSave.pos());
            if (be instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) {
                return;
            }
        }
        if (payload instanceof SlotConfigSavePayload configSave) {
            net.minecraft.world.level.block.entity.BlockEntity be = net.minecraft.client.Minecraft.getInstance().level.getBlockEntity(configSave.pos());
            if (be instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) {
                // Clipboard paste not supported for EV at the moment, just ignore to prevent crash
                return;
            }
        }
        
        // If it's a real TR machine, or another packet, send it normally
        ClientPlayNetworking.send(payload);
    }
}
