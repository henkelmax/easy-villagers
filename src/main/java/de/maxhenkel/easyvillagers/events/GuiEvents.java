package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.ClientConfig;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.CycleTradesButton;
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuiEvents {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onOpenScreen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        if (!(event.getGui() instanceof MerchantScreen)) {
            return;
        }

        ClientConfig.CycleTradesButtonLocation loc = Main.CLIENT_CONFIG.cycleTradesButtonLocation.get();

        if (loc.equals(ClientConfig.CycleTradesButtonLocation.NONE)) {
            return;
        }

        MerchantScreen merchantScreen = (MerchantScreen) event.getGui();

        int posX;

        switch (loc) {
            case TOP_LEFT:
            default:
                posX = merchantScreen.getGuiLeft() + 107;
                break;
            case TOP_RIGHT:
                posX = merchantScreen.getGuiLeft() + 250;
                break;
        }

        event.addWidget(new CycleTradesButton(posX, merchantScreen.getGuiTop() + 8, b -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageCycleTrades());
        }, merchantScreen));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (event.getKey() != Main.CYCLE_TRADES_KEY.getKey().getValue() || event.getAction() != 0) {
            return;
        }

        if (!Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Screen currentScreen = mc.screen;

        if (!(currentScreen instanceof MerchantScreen)) {
            return;
        }

        MerchantScreen screen = (MerchantScreen) currentScreen;

        if (!screen.getMenu().showProgressBar() || screen.getMenu().getTraderXp() > 0) {
            return;
        }

        Main.SIMPLE_CHANNEL.sendToServer(new MessageCycleTrades());
        mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
    }

    public static void onCycleTrades(ServerPlayer player) {
        if (!Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        if (!(player.containerMenu instanceof MerchantMenu)) {
            return;
        }
        MerchantMenu container = (MerchantMenu) player.containerMenu;

        if (container.trader.getVillagerXp() > 0) {
            return;
        }

        if (!(container.trader instanceof Villager)) {
            return;
        }
        Villager villager = (Villager) container.trader;
        villager.offers = null;
        EasyVillagerEntity.recalculateOffers(villager);
        player.sendMerchantOffers(container.containerId, villager.getOffers(), villager.getVillagerData().getLevel(), villager.getVillagerXp(), villager.showProgressBar(), villager.canRestock());
    }

}
