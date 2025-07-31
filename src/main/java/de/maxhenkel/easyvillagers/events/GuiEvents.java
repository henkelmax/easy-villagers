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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class GuiEvents {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onOpenScreen(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof MerchantScreen merchantScreen)) {
            return;
        }
        if (Minecraft.getInstance().player == null) {
            return;
        }
        if (!Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        ClientConfig.CycleTradesButtonLocation loc = Main.CLIENT_CONFIG.cycleTradesButtonLocation.get();

        if (loc.equals(ClientConfig.CycleTradesButtonLocation.NONE)) {
            return;
        }

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

        event.addListener(new CycleTradesButton(posX, merchantScreen.getGuiTop() + 8, b -> {
            PacketDistributor.sendToServer(new MessageCycleTrades());
        }, merchantScreen));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        if (event.getKey() != Main.CYCLE_TRADES_KEY.getKey().getValue() || event.getAction() != 0) {
            return;
        }

        if (!Main.SERVER_CONFIG.getConfigSpec().isLoaded() || !Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        Screen currentScreen = mc.screen;

        if (!(currentScreen instanceof MerchantScreen)) {
            return;
        }

        MerchantScreen screen = (MerchantScreen) currentScreen;

        if (!CycleTradesButton.canCycle(screen.getMenu())) {
            return;
        }

        PacketDistributor.sendToServer(new MessageCycleTrades());
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

        if (container.getTraderXp() > 0 && container.tradeContainer.getActiveOffer() != null) {
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
