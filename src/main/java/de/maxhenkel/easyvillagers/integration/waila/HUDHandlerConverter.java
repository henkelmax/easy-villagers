package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class HUDHandlerConverter implements IBlockComponentProvider {

    public static final HUDHandlerConverter INSTANCE = new HUDHandlerConverter();

    private static final ResourceLocation UID = new ResourceLocation(Main.MODID, "converter");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof ConverterTileentity converter) {
            EasyVillagerEntity villagerEntity = converter.getVillagerEntity();
            if (villagerEntity != null) {
                if (converter.getTimer() >= ConverterTileentity.getZombifyTime() && converter.getTimer() < ConverterTileentity.getConvertTime()) {
                    VillagerProfession profession = villagerEntity.getVillagerData().getProfession();
                    if (profession.equals(VillagerProfession.NONE)) {
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
    public ResourceLocation getUid() {
        return UID;
    }
}