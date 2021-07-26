package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.*;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class PluginEasyVillagers implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, TraderBlock.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, AutoTraderBlock.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, IronFarmBlock.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, FarmerBlock.class);
        registrar.registerComponentProvider(HUDHandlerVillager.INSTANCE, TooltipPosition.BODY, IncubatorBlock.class);
        registrar.registerComponentProvider(HUDHandlerConverter.INSTANCE, TooltipPosition.BODY, ConverterBlock.class);
        registrar.registerComponentProvider(HUDHandlerBreeder.INSTANCE, TooltipPosition.BODY, BreederBlock.class);

        registrar.registerIconProvider(HUDHandlerVillager.INSTANCE, TraderBlock.class);
        registrar.registerIconProvider(HUDHandlerVillager.INSTANCE, AutoTraderBlock.class);
        registrar.registerIconProvider(HUDHandlerVillager.INSTANCE, IronFarmBlock.class);
        registrar.registerIconProvider(HUDHandlerVillager.INSTANCE, FarmerBlock.class);
        registrar.registerIconProvider(HUDHandlerVillager.INSTANCE, IncubatorBlock.class);
        registrar.registerIconProvider(HUDHandlerVillager.INSTANCE, ConverterBlock.class);
        registrar.registerIconProvider(HUDHandlerVillager.INSTANCE, BreederBlock.class);
    }

}
