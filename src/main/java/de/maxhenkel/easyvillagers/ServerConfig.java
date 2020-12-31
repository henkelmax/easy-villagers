package de.maxhenkel.easyvillagers;

import de.maxhenkel.corelib.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig extends ConfigBase {

    public final ForgeConfigSpec.IntValue breedingTime;
    public final ForgeConfigSpec.IntValue convertingTime;
    public final ForgeConfigSpec.IntValue farmSpeed;
    public final ForgeConfigSpec.IntValue golemSpawnTime;
    public final ForgeConfigSpec.IntValue traderMinRestockTime;
    public final ForgeConfigSpec.IntValue traderMaxRestockTime;
    public final ForgeConfigSpec.BooleanValue villagerInventorySounds;
    public final ForgeConfigSpec.IntValue incubatorSpeed;

    public ServerConfig(ForgeConfigSpec.Builder builder) {
        super(builder);

        breedingTime = builder
                .comment("The time in ticks the breeder takes to create a new villager")
                .defineInRange("breeder.breeding_time", 20 * 60, 20, Integer.MAX_VALUE);

        convertingTime = builder
                .comment("The time in ticks the converter takes to convert a villager")
                .defineInRange("converter.converting_time", 20 * 60 * 5, 20, Integer.MAX_VALUE);

        farmSpeed = builder
                .comment("The chance that a crop grows a stage in a farmer")
                .comment("Lower values mean faster growth")
                .defineInRange("farmer.farm_speed", 10, 1, Integer.MAX_VALUE);

        golemSpawnTime = builder
                .comment("The time in ticks the iron farm takes to spawn a golem")
                .defineInRange("iron_farm.spawn_time", 20 * 60 * 4, 20 * 10 + 1, Integer.MAX_VALUE);

        traderMinRestockTime = builder
                .comment("The minimum amount of time in ticks the trader takes to restock")
                .defineInRange("trader.min_restock_time", 20 * 60, 1, Integer.MAX_VALUE);

        traderMaxRestockTime = builder
                .comment("The maximum amount of time in ticks the trader takes to restock")
                .defineInRange("trader.max_restock_time", 20 * 60 * 3, 2, Integer.MAX_VALUE);

        villagerInventorySounds = builder
                .comment("If villagers should make sounds while in the players inventory")
                .define("villager.inventory_sounds", true);

        incubatorSpeed = builder
                .comment("The speed at which the incubator ages the villagers")
                .defineInRange("incubator.speed", 2, 1, 1024);
    }

}
