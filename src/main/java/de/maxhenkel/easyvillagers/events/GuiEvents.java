package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.EasyVillagersClientMod;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.config.ModConfig;
import de.maxhenkel.easyvillagers.gui.CycleTradesButton;
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class GuiEvents {

    public static void init() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof MerchantScreen merchantScreen)) {
                return;
            }
            if (client.player == null) {
                return;
            }
            if (!EasyVillagersMod.CONFIG.server.tradeCycling.get()) {
                return;
            }

            ModConfig.CycleTradesButtonLocation loc = EasyVillagersMod.CONFIG.client.cycleTradesButtonLocation;

            if (loc.equals(ModConfig.CycleTradesButtonLocation.NONE)) {
                return;
            }

            int posX;

            switch (loc) {
                case TOP_LEFT:
                default:
                    posX = (merchantScreen.width - 276) / 2 + 107;
                    break;
                case TOP_RIGHT:
                    posX = (merchantScreen.width - 276) / 2 + 250;
                    break;
            }

            Screens.getWidgets(screen).add(new CycleTradesButton(posX, (merchantScreen.height - 166) / 2 + 8, b -> {
                ClientPlayNetworking.send(new MessageCycleTrades());
            }, merchantScreen));

            net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents.allowKeyPress(screen).register((scr, keyEvent) -> {
                if (keyEvent.key() == EasyVillagersClientMod.CYCLE_TRADES_KEY.getDefaultKey().getValue() && CycleTradesButton.canCycle(merchantScreen.getMenu())) {
                    ClientPlayNetworking.send(new MessageCycleTrades());
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
                    return false;
                }
                return true;
            });
        });
    }
}
