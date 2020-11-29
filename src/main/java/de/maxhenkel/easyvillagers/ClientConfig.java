package de.maxhenkel.easyvillagers;

import de.maxhenkel.corelib.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig extends ConfigBase {

    public final ForgeConfigSpec.BooleanValue enableRightClickPickup;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        super(builder);

        enableRightClickPickup = builder
                .comment("If villagers should be able to be picked up by sneaking and right-clicking")
                .define("villager.sneak_pick_up", true);
    }

}
