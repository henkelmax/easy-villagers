package de.maxhenkel.easyvillagers.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class VillagerBlockBase extends HorizontalRotatableBlock {

    public VillagerBlockBase(Properties properties) {
        super(properties);
    }

    public boolean overrideClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit, Event.Result itemResult) {
        return player.isSneaking() && player.getHeldItemMainhand().isEmpty();
    }

}
