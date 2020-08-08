package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LeadItem;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VillagerEvents {

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof VillagerEntity)) {
            return;
        }

        VillagerEntity villager = (VillagerEntity) event.getTarget();
        PlayerEntity player = event.getPlayer();

        if (player.world.isRemote || !player.isSneaking()) {
            return;
        }

        if (villager.removed || villager.getLeashed()) {
            event.setCancellationResult(ActionResultType.FAIL);
            event.setCanceled(true);
            return;
        }

        ItemStack heldItem = player.getHeldItem(event.getHand());

        if (heldItem.getItem() instanceof LeadItem) {
            villager.setLeashHolder(player, true);
            ItemUtils.decrItemStack(heldItem, player);
            event.setCancellationResult(ActionResultType.CONSUME);
            event.setCanceled(true);
            return;
        }

        ItemStack stack = new ItemStack(ModItems.VILLAGER);

        ModItems.VILLAGER.setVillager(stack, villager);

        if (player.inventory.addItemStackToInventory(stack)) {
            villager.remove();
        }
        event.setCancellationResult(ActionResultType.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getTarget() instanceof VillagerEntity)) {
            return;
        }

        VillagerEntity villager = (VillagerEntity) event.getTarget();

        if (villager.getLeashed()) {
            event.setCancellationResult(ActionResultType.FAIL);
            event.setCanceled(true);
        }
    }

}
