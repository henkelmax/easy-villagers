package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.*;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

@WailaPlugin
public class PluginEasyVillagers implements IWailaPlugin {

    static final ResourceLocation OBJECT_NAME_TAG = new ResourceLocation("waila", "object_name");
    static final ResourceLocation CONFIG_SHOW_REGISTRY = new ResourceLocation("waila", "show_registry");
    static final ResourceLocation REGISTRY_NAME_TAG = new ResourceLocation("waila", "registry_name");

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, TraderTileentity.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, IronFarmTileentity.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, FarmerTileentity.class);

        registrar.registerComponentProvider(HUDHandlerConverter.INSTANCE, TooltipPosition.BODY, ConverterTileentity.class);

        registrar.registerComponentProvider(HUDHandlerBreeder.INSTANCE, TooltipPosition.BODY, BreederTileentity.class);
    }

    public static IFormattableTextComponent getVillagerName(VillagerProfession profession) {
        return new TranslationTextComponent("entity.minecraft.villager." + profession);
    }

    @Nullable
    public static IFormattableTextComponent getVillager(VillagerEntity villager) {
        if (villager != null) {
            VillagerData villagerData = villager.getVillagerData();
            VillagerProfession profession = villagerData.getProfession();
            if (profession.equals(VillagerProfession.NONE) || profession.equals(VillagerProfession.NITWIT)) {
                return PluginEasyVillagers.getVillagerName(profession).func_240699_a_(TextFormatting.GRAY);
            } else {
                return new TranslationTextComponent("tooltip.easy_villagers.villager_profession", PluginEasyVillagers.getVillagerName(profession), new TranslationTextComponent("merchant.level." + villagerData.getLevel())).func_240699_a_(TextFormatting.GRAY);
            }
        }
        return null;
    }

}
