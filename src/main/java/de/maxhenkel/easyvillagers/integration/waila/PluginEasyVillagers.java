package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.*;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.util.ResourceLocation;

@WailaPlugin
public class PluginEasyVillagers implements IWailaPlugin {

    static final ResourceLocation OBJECT_NAME_TAG = new ResourceLocation("waila", "object_name");
    static final ResourceLocation CONFIG_SHOW_REGISTRY = new ResourceLocation("waila", "show_registry");
    static final ResourceLocation REGISTRY_NAME_TAG = new ResourceLocation("waila", "registry_name");

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, TraderTileentity.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, AutoTraderTileentity.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, IronFarmTileentity.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, FarmerTileentity.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, IncubatorTileentity.class);

        registrar.registerComponentProvider(HUDHandlerConverter.INSTANCE, TooltipPosition.BODY, ConverterTileentity.class);

        registrar.registerComponentProvider(HUDHandlerBreeder.INSTANCE, TooltipPosition.BODY, BreederTileentity.class);

        registrar.registerStackProvider(HUDHandlerVillager.INSTANCE, TraderTileentity.class);
        registrar.registerStackProvider(HUDHandlerVillager.INSTANCE, AutoTraderTileentity.class);
        registrar.registerStackProvider(HUDHandlerVillager.INSTANCE, IronFarmTileentity.class);
        registrar.registerStackProvider(HUDHandlerVillager.INSTANCE, FarmerTileentity.class);
        registrar.registerStackProvider(HUDHandlerVillager.INSTANCE, IncubatorTileentity.class);
        registrar.registerStackProvider(HUDHandlerVillager.INSTANCE, ConverterTileentity.class);
        registrar.registerStackProvider(HUDHandlerVillager.INSTANCE, BreederTileentity.class);
    }


}
