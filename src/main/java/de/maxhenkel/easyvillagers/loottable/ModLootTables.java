package de.maxhenkel.easyvillagers.loottable;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModLootTables {

    private static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.LOOT_FUNCTION_TYPE, Main.MODID);
    public static DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<CopyBlockEntityData>> COPY_BLOCK_ENTITY = LOOT_FUNCTION_TYPE_REGISTER.register("copy_block_entity", () -> new LootItemFunctionType<>(CopyBlockEntityData.CODEC));

    public static void init(IEventBus eventBus) {
        LOOT_FUNCTION_TYPE_REGISTER.register(eventBus);
    }
}
