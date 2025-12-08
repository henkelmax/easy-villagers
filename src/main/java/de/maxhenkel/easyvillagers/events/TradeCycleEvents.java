package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.inventory.MerchantMenu;

public class TradeCycleEvents {

    public static void onCycleTrades(ServerPlayer player) {
        if (!EasyVillagersMod.SERVER_CONFIG.tradeCycling.get()) {
            return;
        }

        if (!(player.containerMenu instanceof MerchantMenu)) {
            return;
        }
        MerchantMenu container = (MerchantMenu) player.containerMenu;

        if (container.getTraderXp() > 0 && container.tradeContainer.getActiveOffer() != null) {
            return;
        }

        if (!(container.trader instanceof Villager villager)) {
            return;
        }
        villager.offers = null;
        EasyVillagerEntity.recalculateOffers(villager);
        player.sendMerchantOffers(container.containerId, villager.getOffers(), villager.getVillagerData().level(), villager.getVillagerXp(), villager.showProgressBar(), villager.canRestock());
    }

}
