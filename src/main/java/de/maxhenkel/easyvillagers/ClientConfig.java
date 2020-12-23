package de.maxhenkel.easyvillagers;

import de.maxhenkel.corelib.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig extends ConfigBase {

    public final ForgeConfigSpec.BooleanValue enableRightClickPickup;
    public final ForgeConfigSpec.DoubleValue villagerVolume;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        super(builder);

        enableRightClickPickup = builder
                .comment("If villagers should be able to be picked up by sneaking and right-clicking")
                .define("villager.sneak_pick_up", true);
        villagerVolume = builder
                .comment("The volume of every villager related sound in this mod")
                .defineInRange("villager.volume", 1D, 0D, 1D);
    }

}
