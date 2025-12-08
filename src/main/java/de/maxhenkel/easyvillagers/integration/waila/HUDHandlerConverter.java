package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class HUDHandlerConverter implements IBlockComponentProvider {

    public static final HUDHandlerConverter INSTANCE = new HUDHandlerConverter();

    private static final Identifier UID = Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "converter");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof ConverterTileentity converter) {
            EasyVillagerEntity villagerEntity = converter.getVillagerEntity();
            if (villagerEntity != null) {
                if (converter.getTimer() >= ConverterTileentity.getZombifyTime() && converter.getTimer() < ConverterTileentity.getConvertTime()) {
                    Holder<VillagerProfession> profession = villagerEntity.getVillagerData().profession();
                    if (profession.is(VillagerProfession.NONE)) {
                        iTooltip.add(Component.translatable("entity.minecraft.zombie_villager"));
                    } else {
                        iTooltip.add(Component.translatable("tooltip.easy_villagers.zombie_villager_profession",
                                Component.translatable("entity.minecraft.zombie_villager"),
                                villagerEntity.getAdvancedName()
                        ));
                    }
                } else {
                    Component villager = villagerEntity.getAdvancedName();
                    if (villager != null) {
                        iTooltip.add(villager);
                    }
                }
            }
        }
    }

    @Override
    public Identifier getUid() {
        return UID;
    }
}