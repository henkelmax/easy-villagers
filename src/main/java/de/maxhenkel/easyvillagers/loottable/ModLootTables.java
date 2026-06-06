package de.maxhenkel.easyvillagers.loottable;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class ModLootTables {

    public static final MapCodec<CopyBlockEntityData> COPY_BLOCK_ENTITY = Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "copy_block_entity"), CopyBlockEntityData.CODEC);

    public static void init() {
    }
}
