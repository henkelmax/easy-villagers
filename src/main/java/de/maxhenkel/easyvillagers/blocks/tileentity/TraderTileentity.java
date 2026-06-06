package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TraderTileentity extends TraderTileentityBase {

    public TraderTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.TRADER, ModBlocks.TRADER.defaultBlockState(), pos, state);
    }

}
