package de.maxhenkel.easyvillagers;

import de.maxhenkel.corelib.config.ConfigBase;
import de.maxhenkel.corelib.tag.Tag;
import de.maxhenkel.corelib.tag.TagUtils;
import net.minecraft.world.item.Item;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServerConfig extends ConfigBase {

    public final ModConfigSpec.IntValue breedingTime;
    public final ModConfigSpec.IntValue convertingTime;
    public final ModConfigSpec.IntValue farmSpeed;
    private final ModConfigSpec.ConfigValue<List<? extends String>> farmCropsBlacklistSpec;
    public final ModConfigSpec.IntValue golemSpawnTime;
    public final ModConfigSpec.IntValue traderMinRestockTime;
    public final ModConfigSpec.IntValue traderMaxRestockTime;
    public final ModConfigSpec.IntValue autoTraderMinRestockTime;
    public final ModConfigSpec.IntValue autoTraderMaxRestockTime;
    public final ModConfigSpec.IntValue autoTraderCooldown;
    public final ModConfigSpec.BooleanValue villagerInventorySounds;
    public final ModConfigSpec.IntValue villagerSoundAmount;
    public final ModConfigSpec.IntValue incubatorSpeed;
    public final ModConfigSpec.BooleanValue tradeCycling;
    public final ModConfigSpec.BooleanValue universalReputation;

    public List<Tag<Item>> farmCropsBlacklist;

    public ServerConfig(ModConfigSpec.Builder builder) {
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

        farmCropsBlacklistSpec = builder
                .comment("The crops that the farmer will not use", "Entries starting with '#' are treated as tags")
                .defineList("farmer.crop_blacklist", Arrays.asList(
                        "#easy_villagers:invalid_farmer_crop"
                ), Objects::nonNull);

        golemSpawnTime = builder
                .comment("The time in ticks the iron farm takes to spawn a golem")
                .defineInRange("iron_farm.spawn_time", 20 * 60 * 4, 20 * 10 + 1, Integer.MAX_VALUE);

        traderMinRestockTime = builder
                .comment("The minimum amount of time in ticks the trader takes to restock")
                .defineInRange("trader.min_restock_time", 20 * 60, 1, Integer.MAX_VALUE);

        traderMaxRestockTime = builder
                .comment("The maximum amount of time in ticks the trader takes to restock")
                .defineInRange("trader.max_restock_time", 20 * 60 * 3, 2, Integer.MAX_VALUE);

        autoTraderMinRestockTime = builder
                .comment("The minimum amount of time in ticks the auto trader takes to restock")
                .defineInRange("auto_trader.min_restock_time", 20 * 60, 1, Integer.MAX_VALUE);

        autoTraderMaxRestockTime = builder
                .comment("The maximum amount of time in ticks the auto trader takes to restock")
                .defineInRange("auto_trader.max_restock_time", 20 * 60 * 3, 2, Integer.MAX_VALUE);

        autoTraderCooldown = builder
                .comment("The cooldown in ticks for the auto trader to do a trade")
                .defineInRange("auto_trader.trade_cooldown", 20, 1, Integer.MAX_VALUE);

        villagerInventorySounds = builder
                .comment("If villagers should make sounds while in the players inventory")
                .define("villager.inventory_sounds", true);

        villagerSoundAmount = builder
                .comment("How frequent a villager block should make a villager sound", "Lower values mean more frequent sounds")
                .defineInRange("villager.sound_amount", 20, 1, Integer.MAX_VALUE);

        incubatorSpeed = builder
                .comment("The speed at which the incubator ages the villagers")
                .defineInRange("incubator.speed", 2, 1, 1024);

        tradeCycling = builder
                .comment("If the trade cycling button should be enabled")
                .define("villager.trade_cycling", true);

        universalReputation = builder
                .comment(
                        "If the villager reputation should be the same for every player",
                        "This affects the prices of cured/converted villagers and the prices of the auto trader"
                )
                .define("villager.universal_reputation", true);

    }

    @Override
    public void onLoad(ModConfigEvent.Loading evt) {
        super.onLoad(evt);
        onConfigChange();
    }

    @Override
    public void onReload(ModConfigEvent.Reloading event) {
        super.onReload(event);
        onConfigChange();
    }

    private void onConfigChange() {
        farmCropsBlacklist = farmCropsBlacklistSpec.get().stream().map(s -> TagUtils.getItem(s, true)).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
