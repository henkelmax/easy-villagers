package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class BlockEvents {

    @SubscribeEvent
    public void onBlockClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (!(state.getBlock() instanceof VillagerBlockBase)) {
            return;
        }
        VillagerBlockBase block = (VillagerBlockBase) state.getBlock();

        if (block.overrideClick(state, event.getLevel(), event.getPos(), event.getEntity(), event.getHand())) {
            event.setUseBlock(TriState.TRUE);
        }
    }

}
