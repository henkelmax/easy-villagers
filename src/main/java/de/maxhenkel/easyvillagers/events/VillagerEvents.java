package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LeadItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.TickEvent;
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

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.phase.equals(TickEvent.Phase.END)) {
            return;
        }

        if (event.player.world.getGameTime() % 20 != 0) {
            return;
        }
        for (Hand hand : Hand.values()) {
            ItemStack heldItem = event.player.getHeldItem(hand);
            if (heldItem.getItem() instanceof VillagerItem && event.player.world.rand.nextInt(20) == 0) {
                event.player.playSound(SoundEvents.ENTITY_VILLAGER_AMBIENT, SoundCategory.NEUTRAL, 1F, 1F);
            }
        }
    }

}
