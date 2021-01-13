package de.maxhenkel.easyvillagers.entity;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.GossipManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

public class EasyVillagerEntity extends VillagerEntity {

    public static final Field UUID_GOSSIPS_MAPPING = ObfuscationReflectionHelper.findField(GossipManager.class, "field_220928_a");

    public EasyVillagerEntity(EntityType<? extends VillagerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EasyVillagerEntity(EntityType<? extends VillagerEntity> type, World worldIn, IVillagerType villagerType) {
        super(type, worldIn, villagerType);
    }

    @Override
    public int getPlayerReputation(PlayerEntity player) {
        if (Main.SERVER_CONFIG.universalReputation.get()) {
            return getUniversalReputation(this);
        } else {
            return super.getPlayerReputation(player);
        }
    }

    public static int getReputation(VillagerEntity villager) {
        if (Main.SERVER_CONFIG.universalReputation.get()) {
            return getUniversalReputation(villager);
        } else {
            return 0;
        }
    }

    public static int getUniversalReputation(VillagerEntity villager) {
        try {
            Map<UUID, Object> map = (Map<UUID, Object>) UUID_GOSSIPS_MAPPING.get(villager.getGossip());
            return map.keySet().stream().map(uuid -> villager.getGossip().getReputation(uuid, (gossipType) -> true)).reduce(0, Integer::sum);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void recalculateOffers() {
        resetOffers(this);
        calculateOffers(this);
    }

    public static void recalculateOffers(VillagerEntity villager) {
        resetOffers(villager);
        calculateOffers(villager);
    }

    private static void resetOffers(VillagerEntity villager) {
        for (MerchantOffer merchantoffer : villager.getOffers()) {
            merchantoffer.resetSpecialPrice();
        }
    }

    private static void calculateOffers(VillagerEntity villager) {
        int i = getReputation(villager);
        if (i != 0) {
            for (MerchantOffer merchantoffer : villager.getOffers()) {
                merchantoffer.increaseSpecialPrice(-MathHelper.floor((float) i * merchantoffer.getPriceMultiplier()));
            }
        }
    }

}
