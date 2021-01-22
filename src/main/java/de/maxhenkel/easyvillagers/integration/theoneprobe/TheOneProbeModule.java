package de.maxhenkel.easyvillagers.integration.theoneprobe;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import java.util.function.Function;

public class TheOneProbeModule implements Function<ITheOneProbe, Void> {

    @SubscribeEvent
    public static void enqueueIMC(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TheOneProbeModule::new);
        }
    }

    @Override
    public Void apply(ITheOneProbe input) {
        input.registerProvider(new TileInfoProvider());
        return null;
    }

}