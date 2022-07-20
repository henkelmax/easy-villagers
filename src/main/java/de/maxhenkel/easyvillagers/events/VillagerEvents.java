package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VillagerEvents {

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.EntityInteract event) {
        if (!event.getWorld().isClientSide) {
            return;
        }
        if (!(event.getTarget() instanceof Villager)) {
            return;
        }

        if (!Main.CLIENT_CONFIG.enableRightClickPickup.get()) {
            return;
        }

        Villager villager = (Villager) event.getTarget();
        Player player = event.getPlayer();

        if (!player.isShiftKeyDown()) {
            return;
        }

        if (!arePickupConditionsMet(villager)) {
            return;
        }

        Main.SIMPLE_CHANNEL.sendToServer(new MessagePickUpVillager(villager.getUUID()));

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Main.PICKUP_KEY.consumeClick()) {
            return;
        }

        Entity pointedEntity = Minecraft.getInstance().crosshairPickEntity;

        if (!(pointedEntity instanceof Villager villager) || !arePickupConditionsMet(villager)) {
            return;
        }

        Main.SIMPLE_CHANNEL.sendToServer(new MessagePickUpVillager(villager.getUUID()));
    }

    public static void pickUp(Villager villager, Player player) {
        if (!arePickupConditionsMet(villager)) {
            return;
        }

        ItemStack stack = new ItemStack(ModItems.VILLAGER);

        ModItems.VILLAGER.setVillager(stack, villager);

        if (player.getMainHandItem().isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
            villager.discard();
        } else {
            if (player.getInventory().add(stack)) {
                villager.discard();
            }
        }
    }

    public static boolean arePickupConditionsMet(Villager villager) {
        if (!villager.isAlive()) {
            return false;
        }
        if (villager.isSleeping()) {
            return false;
        }

        return true;
    }

}
