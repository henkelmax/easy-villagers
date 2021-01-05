package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockEvents {

    @SubscribeEvent
    public void onBlockClick(PlayerInteractEvent.RightClickBlock event) {
        BlockRayTraceResult hitVec = event.getHitVec();
        BlockState state = event.getWorld().getBlockState(hitVec.getPos());
        if (!(state.getBlock() instanceof VillagerBlockBase)) {
            return;
        }
        VillagerBlockBase block = (VillagerBlockBase) state.getBlock();

        if (block.overrideClick(state, event.getWorld(), hitVec.getPos(), event.getPlayer(), event.getHand(), hitVec, event.getUseItem())) {
            event.setUseBlock(Event.Result.ALLOW);
        }
    }

}
