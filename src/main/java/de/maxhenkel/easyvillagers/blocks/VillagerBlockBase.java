package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.VoxelUtils;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class VillagerBlockBase extends HorizontalRotatableBlock implements EntityBlock {

    private static final VoxelShape SHAPE = VoxelUtils.combine(
            Block.box(0D, 0D, 0D, 16D, 1D, 16D),
            Block.box(0D, 15D, 0D, 16D, 16D, 16D),
            Block.box(0D, 0D, 0D, 1D, 16D, 16D),
            Block.box(15D, 0D, 0D, 16D, 16D, 16D),
            Block.box(0D, 0D, 0D, 16D, 16D, 1D),
            Block.box(0D, 0D, 15D, 16D, 16D, 16D)
    );

    public VillagerBlockBase(Properties properties) {
        super(properties);
    }

    public boolean overrideClick(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn) {
        return player.isShiftKeyDown() && player.getMainHandItem().isEmpty();
    }

    public static void playRandomVillagerSound(Level world, BlockPos pos, SoundEvent soundEvent) {
        if (world.getGameTime() % Main.SERVER_CONFIG.villagerSoundAmount.get() == 0 && world.random.nextInt(40) == 0) {
            playVillagerSound(world, pos, soundEvent);
        }
    }

    public static void playRandomVillagerSound(Player player, SoundEvent soundEvent) {
        if (player.level().getGameTime() % Main.SERVER_CONFIG.villagerSoundAmount.get() == 0 && player.level().random.nextInt(40) == 0) {
            player.playNotifySound(soundEvent, SoundSource.BLOCKS, 1F, 1F);
        }
    }

    public static void playVillagerSound(Level world, BlockPos pos, SoundEvent soundEvent) {
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundSource.BLOCKS, 1F, 1F);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

}
