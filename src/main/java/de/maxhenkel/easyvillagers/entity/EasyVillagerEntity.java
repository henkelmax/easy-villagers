package de.maxhenkel.easyvillagers.entity;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EasyVillagerEntity extends Villager {

    public EasyVillagerEntity(EntityType<? extends Villager> type, Level level) {
        super(type, level);
    }

    public EasyVillagerEntity(EntityType<? extends Villager> type, Level level, ResourceKey<VillagerType> villagerTypeResourceKey) {
        super(type, level, villagerTypeResourceKey);
    }

    public EasyVillagerEntity(EntityType<? extends Villager> type, Level level, Holder<VillagerType> villagerTypeHolder) {
        super(type, level, villagerTypeHolder);
    }

    @Override
    public int getPlayerReputation(Player player) {
        if (EasyVillagersMod.SERVER_CONFIG.universalReputation.get()) {
            return getUniversalReputation(this);
        } else {
            return super.getPlayerReputation(player);
        }
    }

    public static int getReputation(Villager villager) {
        if (EasyVillagersMod.SERVER_CONFIG.universalReputation.get()) {
            return getUniversalReputation(villager);
        } else {
            return 0;
        }
    }

    public static int getUniversalReputation(Villager villager) {
        return villager.getGossips().getGossipEntries().keySet().stream().map(uuid -> villager.getGossips().getReputation(uuid, EasyVillagerEntity::isPositive)).reduce(0, Integer::sum);
    }

    public static boolean isPositive(GossipType gossipType) {
        return switch (gossipType) {
            case MAJOR_NEGATIVE, MINOR_NEGATIVE -> false;
            default -> true;
        };
    }

    public void recalculateOffers() {
        resetOffers(this);
        calculateOffers(this);
    }

    @Override
    public int getAge() {
        if (level() != null && level().isClientSide) {
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
        Holder<VillagerProfession> profession = villagerData.profession();
        if (profession.is(VillagerProfession.NONE)) {
            return EntityType.VILLAGER.getDescription().copy();
        } else {
            return getTypeName();
        }
    }

    public Component getAdvancedName() {
        return Component.translatable("tooltip.easy_villagers.villager_profession", getName().copy(), Component.translatable("merchant.level." + getVillagerData().level())).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
    }

    @Override
    public void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
    }
}
