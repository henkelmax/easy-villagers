package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;

public class VillagerBlockBase extends HorizontalRotatableBlock {

    public VillagerBlockBase(Properties properties) {
        super(properties);
    }

    public boolean overrideClick(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, Event.Result itemResult) {
        return player.isShiftKeyDown() && player.getMainHandItem().isEmpty();
    }

    public static void playRandomVillagerSound(Level world, BlockPos pos, SoundEvent soundEvent) {
        if (world.getGameTime() % Main.SERVER_CONFIG.villagerSoundAmount.get() == 0 && world.random.nextInt(40) == 0) {
            playVillagerSound(world, pos, soundEvent);
        }
    }

    public static void playRandomVillagerSound(Player player, SoundEvent soundEvent) {
        if (player.level.getGameTime() % Main.SERVER_CONFIG.villagerSoundAmount.get() == 0 && player.level.random.nextInt(40) == 0) {
            player.playNotifySound(soundEvent, SoundSource.BLOCKS, 1F, 1F);
        }
    }

    public static void playVillagerSound(Level world, BlockPos pos, SoundEvent soundEvent) {
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundSource.BLOCKS, 1F, 1F);
    }

}
