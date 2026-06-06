package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class TestProfession {
    public static void runTest() {
        Optional<Holder<PoiType>> poiTypeHolder = PoiTypes.forState(Blocks.LECTERN.defaultBlockState());
        EasyVillagersMod.LOGGER.info("poiTypeHolder present: " + poiTypeHolder.isPresent());
        if (!poiTypeHolder.isEmpty()) {
            Holder<PoiType> poiType = poiTypeHolder.get();
            EasyVillagersMod.LOGGER.info("poiType key: " + poiType.unwrapKey().map(k -> k.toString()).orElse("NO_KEY"));
            for (VillagerProfession profession : BuiltInRegistries.VILLAGER_PROFESSION) {
                if (profession.heldJobSite().test(poiType)) {
                    EasyVillagersMod.LOGGER.info("Matched profession: " + BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession));
                }
            }
        }
        
        // Test VillagerData.of
        // We can't create level but we can use null for level in createEasyVillager? 
        // No, we need a level. We will just test it in-game by running the mod and seeing my previous log statements!
    }
}
