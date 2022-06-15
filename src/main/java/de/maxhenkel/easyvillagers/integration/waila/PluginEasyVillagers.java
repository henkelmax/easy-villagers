package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.*;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class PluginEasyVillagers implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(HUDHandlerVillager.INSTANCE, TraderBlock.class);
        registration.registerBlockComponent(HUDHandlerVillager.INSTANCE, AutoTraderBlock.class);
        registration.registerBlockComponent(HUDHandlerVillager.INSTANCE, IronFarmBlock.class);
        registration.registerBlockComponent(HUDHandlerVillager.INSTANCE, FarmerBlock.class);
        registration.registerBlockComponent(HUDHandlerVillager.INSTANCE, IncubatorBlock.class);
        registration.registerBlockComponent(HUDHandlerConverter.INSTANCE, ConverterBlock.class);
        registration.registerBlockComponent(HUDHandlerBreeder.INSTANCE, BreederBlock.class);

        registration.registerBlockIcon(HUDHandlerVillager.INSTANCE, TraderBlock.class);
        registration.registerBlockIcon(HUDHandlerVillager.INSTANCE, AutoTraderBlock.class);
        registration.registerBlockIcon(HUDHandlerVillager.INSTANCE, IronFarmBlock.class);
        registration.registerBlockIcon(HUDHandlerVillager.INSTANCE, FarmerBlock.class);
        registration.registerBlockIcon(HUDHandlerVillager.INSTANCE, IncubatorBlock.class);
        registration.registerBlockIcon(HUDHandlerVillager.INSTANCE, ConverterBlock.class);
        registration.registerBlockIcon(HUDHandlerVillager.INSTANCE, BreederBlock.class);
    }

}
