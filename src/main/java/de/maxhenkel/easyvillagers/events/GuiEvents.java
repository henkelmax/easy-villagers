package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.ClientConfig;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.CycleTradesButton;
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.MerchantContainer;
import net.minecraft.util.SoundEvents;
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

        if (!CycleTradesButton.canCycle(screen.getMenu())) {
            return;
        }

        Main.SIMPLE_CHANNEL.sendToServer(new MessageCycleTrades());
        mc.getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
    }

    public static void onCycleTrades(ServerPlayerEntity player) {
        if (!Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        if (!(player.containerMenu instanceof MerchantContainer)) {
            return;
        }
        MerchantContainer container = (MerchantContainer) player.containerMenu;

        if (!CycleTradesButton.canCycle(container)) {
            return;
        }

        if (!(container.trader instanceof VillagerEntity)) {
            return;
        }
        VillagerEntity villager = (VillagerEntity) container.trader;
        villager.offers = null;
        EasyVillagerEntity.recalculateOffers(villager);
        player.sendMerchantOffers(container.containerId, villager.getOffers(), villager.getVillagerData().getLevel(), villager.getVillagerXp(), villager.showProgressBar(), villager.canRestock());
    }

}
