package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.ClientConfig;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.gui.CycleTradesButton;
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.MerchantContainer;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class GuiEvents {

    public static final Field MERCHANT = ObfuscationReflectionHelper.findField(MerchantContainer.class, "field_75178_e");
    public static final Field OFFERS = ObfuscationReflectionHelper.findField(AbstractVillagerEntity.class, "field_213724_bz");

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
        if (event.getKey() != Main.CYCLE_TRADES_KEY.getKey().getKeyCode() || event.getAction() != 0) {
            return;
        }

        if (!Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Screen currentScreen = mc.currentScreen;

        if (!(currentScreen instanceof MerchantScreen)) {
            return;
        }

        MerchantScreen screen = (MerchantScreen) currentScreen;

        if (!screen.getContainer().func_217042_i() /* hasXpBar */ || screen.getContainer().getXp() > 0) {
            return;
        }

        Main.SIMPLE_CHANNEL.sendToServer(new MessageCycleTrades());
        mc.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1F));
    }

    public static void onCycleTrades(ServerPlayerEntity player) {
        if (!Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        if (!(player.openContainer instanceof MerchantContainer)) {
            return;
        }
        MerchantContainer container = (MerchantContainer) player.openContainer;
        IMerchant merchant;
        try {
            merchant = (IMerchant) MERCHANT.get(container);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (merchant.getXp() > 0) {
            return;
        }

        if (!(merchant instanceof VillagerEntity)) {
            return;
        }
        VillagerEntity villager = (VillagerEntity) merchant;
        try {
            OFFERS.set(villager, null);
            player.openMerchantContainer(container.windowId, villager.getOffers(), villager.getVillagerData().getLevel(), villager.getXp(), villager.hasXPBar(), villager.canRestockTrades());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
