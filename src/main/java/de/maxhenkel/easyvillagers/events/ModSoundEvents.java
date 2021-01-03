package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModSoundEvents {

    @SubscribeEvent
    public void onSound(PlaySoundAtEntityEvent event) {
        if (event.getSound() != null && event.getCategory() != null && isVillagerSound(event.getSound()) && event.getCategory().equals(SoundCategory.BLOCKS)) {
            event.setVolume(Main.CLIENT_CONFIG.villagerVolume.get().floatValue());
        }
    }

    private boolean isVillagerSound(SoundEvent event) {
        return event.equals(SoundEvents.ENTITY_VILLAGER_NO)
                || event.equals(SoundEvents.ENTITY_VILLAGER_CELEBRATE)
                || event.equals(SoundEvents.ENTITY_VILLAGER_DEATH)
                || event.equals(SoundEvents.ENTITY_VILLAGER_AMBIENT)
                || event.equals(SoundEvents.ENTITY_VILLAGER_HURT)
                || event.equals(SoundEvents.ENTITY_VILLAGER_TRADE)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_ARMORER)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_CARTOGRAPHER)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_CLERIC)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_FARMER)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_FISHERMAN)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_FLETCHER)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_LEATHERWORKER)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_MASON)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_SHEPHERD)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH)
                || event.equals(SoundEvents.ENTITY_VILLAGER_WORK_WEAPONSMITH)
                || event.equals(SoundEvents.ENTITY_VILLAGER_YES)
                || event.equals(SoundEvents.ENTITY_IRON_GOLEM_HURT)
                || event.equals(SoundEvents.ENTITY_IRON_GOLEM_DEATH)
                || event.equals(SoundEvents.ENTITY_ZOMBIE_AMBIENT)
                || event.equals(SoundEvents.ENTITY_ZOMBIE_INFECT)
                || event.equals(SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT)
                || event.equals(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED)
                || event.equals(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE);
    }

}
