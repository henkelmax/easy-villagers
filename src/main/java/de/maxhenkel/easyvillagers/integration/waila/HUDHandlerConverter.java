package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class HUDHandlerConverter implements IComponentProvider {

    static final HUDHandlerConverter INSTANCE = new HUDHandlerConverter();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!(accessor.getTileEntity() instanceof ConverterTileentity)) {
            return;
        }

        ConverterTileentity converter = (ConverterTileentity) accessor.getTileEntity();
        VillagerEntity villagerEntity = converter.getVillagerEntity();
        if (villagerEntity != null) {
            if (converter.getTimer() >= ConverterTileentity.getZombifyTime() && converter.getTimer() < ConverterTileentity.getConvertTime()) {
                VillagerProfession profession = villagerEntity.getVillagerData().getProfession();
                if (profession.equals(VillagerProfession.NONE)) {
                    tooltip.add(new TranslationTextComponent("entity.minecraft.zombie_villager").mergeStyle(TextFormatting.GRAY));
                } else {
                    tooltip.add(new TranslationTextComponent("tooltip.easy_villagers.zombie_villager_profession",
                            new TranslationTextComponent("entity.minecraft.zombie_villager"),
                            PluginEasyVillagers.getVillagerName(profession)
                    ).mergeStyle(TextFormatting.GRAY));
                }
            } else {
                ITextComponent villager = PluginEasyVillagers.getVillager(villagerEntity);
                if (villager != null) {
                    tooltip.add(villager);
                }
            }
        }
    }

}