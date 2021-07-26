package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.npc.VillagerProfession;

public class HUDHandlerConverter implements IComponentProvider {

    public static final HUDHandlerConverter INSTANCE = new HUDHandlerConverter();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof ConverterTileentity converter) {
            EasyVillagerEntity villagerEntity = converter.getVillagerEntity();
            if (villagerEntity != null) {
                if (converter.getTimer() >= ConverterTileentity.getZombifyTime() && converter.getTimer() < ConverterTileentity.getConvertTime()) {
                    VillagerProfession profession = villagerEntity.getVillagerData().getProfession();
                    if (profession.equals(VillagerProfession.NONE)) {
                        iTooltip.add(new TranslatableComponent("entity.minecraft.zombie_villager"));
                    } else {
                        iTooltip.add(new TranslatableComponent("tooltip.easy_villagers.zombie_villager_profession",
                                new TranslatableComponent("entity.minecraft.zombie_villager"),
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
}