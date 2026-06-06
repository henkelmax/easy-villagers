package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEvents {
    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockState state = world.getBlockState(hitResult.getBlockPos());
            if (state.getBlock() instanceof VillagerBlockBase block) {
                if (block.overrideClick(state, world, hitResult.getBlockPos(), player, hand)) {
                    // Manually trigger useWithoutItem because Vanilla bypasses it for empty-hand sneaks
                    InteractionResult result = state.useWithoutItem(world, player, hitResult);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
            return InteractionResult.PASS;
        });
    }
}
