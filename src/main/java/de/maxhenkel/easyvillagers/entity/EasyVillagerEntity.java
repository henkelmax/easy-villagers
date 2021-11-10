package de.maxhenkel.easyvillagers.entity;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;

public class EasyVillagerEntity extends Villager {

    public EasyVillagerEntity(EntityType<? extends Villager> type, Level worldIn) {
        super(type, worldIn);
    }

    public EasyVillagerEntity(EntityType<? extends Villager> type, Level worldIn, VillagerType villagerType) {
        super(type, worldIn, villagerType);
    }

    @Override
    public int getPlayerReputation(Player player) {
        if (Main.SERVER_CONFIG.universalReputation.get()) {
            return getUniversalReputation(this);
        } else {
            return super.getPlayerReputation(player);
        }
    }

    public static int getReputation(Villager villager) {
        if (Main.SERVER_CONFIG.universalReputation.get()) {
            return getUniversalReputation(villager);
        } else {
            return 0;
        }
    }

    public static int getUniversalReputation(Villager villager) {
        return villager.getGossips().getGossipEntries().keySet().stream().map(uuid -> villager.getGossips().getReputation(uuid, (gossipType) -> true)).reduce(0, Integer::sum);
    }

    public void recalculateOffers() {
        resetOffers(this);
        calculateOffers(this);
    }

    @Override
    public int getAge() {
        if (level.isClientSide) {
            return super.getAge() < 0 ? -24000 : 1;
        } else {
            return age;
        }
    }

    public static void recalculateOffers(Villager villager) {
        resetOffers(villager);
        calculateOffers(villager);
    }

    private static void resetOffers(Villager villager) {
        for (MerchantOffer merchantoffer : villager.getOffers()) {
            merchantoffer.resetSpecialPriceDiff();
        }
    }

    private static void calculateOffers(Villager villager) {
        int i = getReputation(villager);
        if (i != 0) {
            for (MerchantOffer merchantoffer : villager.getOffers()) {
                merchantoffer.addToSpecialPriceDiff(-Mth.floor((float) i * merchantoffer.getPriceMultiplier()));
            }
        }
    }

    @Override
    public Component getName() {
        if (hasCustomName()) {
            return super.getName();
        }
        VillagerData villagerData = getVillagerData();
        VillagerProfession profession = villagerData.getProfession();
        if (profession.equals(VillagerProfession.NONE)) {
            return EntityType.VILLAGER.getDescription().copy();
        } else {
            return getTypeName();
        }
    }

    public Component getAdvancedName() {
        return new TranslatableComponent("tooltip.easy_villagers.villager_profession", getName().copy(), new TranslatableComponent("merchant.level." + getVillagerData().getLevel())).withStyle(ChatFormatting.GRAY);
    }

}
