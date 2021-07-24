package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModSoundEvents {

    @SubscribeEvent
    public void onSound(PlaySoundAtEntityEvent event) {
        if (event.getSound() != null && event.getCategory() != null && isVillagerSound(event.getSound()) && event.getCategory().equals(SoundSource.BLOCKS)) {
            event.setVolume(Main.CLIENT_CONFIG.villagerVolume.get().floatValue());
        }
    }

    private boolean isVillagerSound(SoundEvent event) {
        return event.equals(SoundEvents.VILLAGER_NO)
                || event.equals(SoundEvents.VILLAGER_CELEBRATE)
                || event.equals(SoundEvents.VILLAGER_DEATH)
                || event.equals(SoundEvents.VILLAGER_AMBIENT)
                || event.equals(SoundEvents.VILLAGER_HURT)
                || event.equals(SoundEvents.VILLAGER_TRADE)
                || event.equals(SoundEvents.VILLAGER_WORK_ARMORER)
                || event.equals(SoundEvents.VILLAGER_WORK_BUTCHER)
                || event.equals(SoundEvents.VILLAGER_WORK_CARTOGRAPHER)
                || event.equals(SoundEvents.VILLAGER_WORK_CLERIC)
                || event.equals(SoundEvents.VILLAGER_WORK_FARMER)
                || event.equals(SoundEvents.VILLAGER_WORK_FISHERMAN)
                || event.equals(SoundEvents.VILLAGER_WORK_FLETCHER)
                || event.equals(SoundEvents.VILLAGER_WORK_LEATHERWORKER)
                || event.equals(SoundEvents.VILLAGER_WORK_LIBRARIAN)
                || event.equals(SoundEvents.VILLAGER_WORK_MASON)
                || event.equals(SoundEvents.VILLAGER_WORK_SHEPHERD)
                || event.equals(SoundEvents.VILLAGER_WORK_TOOLSMITH)
                || event.equals(SoundEvents.VILLAGER_WORK_WEAPONSMITH)
                || event.equals(SoundEvents.VILLAGER_YES)
                || event.equals(SoundEvents.IRON_GOLEM_HURT)
                || event.equals(SoundEvents.IRON_GOLEM_DEATH)
                || event.equals(SoundEvents.ZOMBIE_AMBIENT)
                || event.equals(SoundEvents.ZOMBIE_INFECT)
                || event.equals(SoundEvents.ZOMBIE_VILLAGER_AMBIENT)
                || event.equals(SoundEvents.ZOMBIE_VILLAGER_CONVERTED)
                || event.equals(SoundEvents.ZOMBIE_VILLAGER_CURE);
    }

}
