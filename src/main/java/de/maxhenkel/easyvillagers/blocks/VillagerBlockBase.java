package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class VillagerBlockBase extends HorizontalRotatableBlock {

    public VillagerBlockBase(Properties properties) {
        super(properties);
    }

    public boolean overrideClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, Event.Result itemResult) {
        return player.isSneaking() && player.getHeldItemMainhand().isEmpty();
    }

    public static void playRandomVillagerSound(World world, BlockPos pos, SoundEvent soundEvent) {
        if (world.getGameTime() % Main.SERVER_CONFIG.villagerSoundAmount.get() == 0 && world.rand.nextInt(40) == 0) {
            playVillagerSound(world, pos, soundEvent);
        }
    }

    public static void playRandomVillagerSound(PlayerEntity player, SoundEvent soundEvent) {
        if (player.world.getGameTime() % Main.SERVER_CONFIG.villagerSoundAmount.get() == 0 && player.world.rand.nextInt(40) == 0) {
            player.playSound(soundEvent, SoundCategory.BLOCKS, 1F, 1F);
        }
    }

    public static void playVillagerSound(World world, BlockPos pos, SoundEvent soundEvent) {
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundCategory.BLOCKS, 1F, 1F);
    }

}
