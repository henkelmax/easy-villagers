package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockEvents {

    @SubscribeEvent
    public void onBlockClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getWorld().getBlockState(event.getPos());
        if (!(state.getBlock() instanceof VillagerBlockBase)) {
            return;
        }
        VillagerBlockBase block = (VillagerBlockBase) state.getBlock();

        if (block.overrideClick(state, event.getWorld(), event.getPos(), event.getPlayer(), event.getHand(), event.getUseItem())) {
            event.setUseBlock(Event.Result.ALLOW);
        }
    }

}
