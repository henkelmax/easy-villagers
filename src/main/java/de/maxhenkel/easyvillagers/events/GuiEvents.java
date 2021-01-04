package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.gui.CycleTradesButton;
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.MerchantContainer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class GuiEvents {

    public static final Field MERCHANT = ObfuscationReflectionHelper.findField(MerchantContainer.class, "field_75178_e");
    public static final Field OFFERS = ObfuscationReflectionHelper.findField(AbstractVillagerEntity.class, "field_213724_bz");

    @SubscribeEvent
    public void onOpenScreen(GuiScreenEvent.InitGuiEvent event) {
        if (!Main.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        if (!(event.getGui() instanceof MerchantScreen)) {
            return;
        }

        MerchantScreen merchantScreen = (MerchantScreen) event.getGui();

        event.addWidget(new CycleTradesButton(merchantScreen.getGuiLeft() + 250, merchantScreen.getGuiTop() + 8, b -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageCycleTrades());
        }, merchantScreen));
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

        if (container.getXp() > 0) {
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
