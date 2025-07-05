package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;

public class ModSoundEvents {

    @SubscribeEvent
    public void onSound(PlayLevelSoundEvent.AtEntity event) {
        if (event.getSound() != null && event.getSource() != null && isVillagerSound(event.getSound().value()) && event.getSource().equals(SoundSource.BLOCKS)) {
            event.setNewVolume(EasyVillagersMod.CLIENT_CONFIG.villagerVolume.get().floatValue());
        }
    }

    @SubscribeEvent
    public void onSound(PlayLevelSoundEvent.AtPosition event) {
        if (event.getSound() != null && event.getSound().value() != null && event.getSource() != null && isVillagerSound(event.getSound().value()) && event.getSource().equals(SoundSource.BLOCKS)) {
            event.setNewVolume(EasyVillagersMod.CLIENT_CONFIG.villagerVolume.get().floatValue());
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
