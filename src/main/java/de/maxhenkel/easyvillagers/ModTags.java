package de.maxhenkel.easyvillagers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static final TagKey<Item> INVALID_FARMER_CROP = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "invalid_farmer_crop"));

}
