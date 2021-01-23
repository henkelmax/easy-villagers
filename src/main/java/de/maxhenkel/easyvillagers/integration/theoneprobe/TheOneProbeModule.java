package de.maxhenkel.easyvillagers.integration.theoneprobe;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TheOneProbeModule implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe input) {
        input.registerProvider(new TileInfoProvider());
        return null;
    }

}