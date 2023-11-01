package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.corelib.net.NetUtils;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class VillagerEvents {

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.EntityInteract event) {
        if (!event.getLevel().isClientSide) {
            return;
        }
        if (!(event.getTarget() instanceof Villager)) {
            return;
        }

        if (!Main.CLIENT_CONFIG.enableRightClickPickup.get()) {
            return;
        }

        Villager villager = (Villager) event.getTarget();
        Player player = event.getEntity();

        if (!player.isShiftKeyDown()) {
            return;
        }

        if (!arePickupConditionsMet(villager)) {
            return;
        }

        NetUtils.sendToServer(Main.SIMPLE_CHANNEL, new MessagePickUpVillager(villager.getUUID()));

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (!Main.PICKUP_KEY.consumeClick()) {
            return;
        }

        Entity pointedEntity = Minecraft.getInstance().crosshairPickEntity;

        if (!(pointedEntity instanceof Villager villager) || !arePickupConditionsMet(villager)) {
            return;
        }

        NetUtils.sendToServer(Main.SIMPLE_CHANNEL, new MessagePickUpVillager(villager.getUUID()));
    }

    public static void pickUp(Villager villager, Player player) {
        if (!arePickupConditionsMet(villager)) {
            return;
        }

        ItemStack stack = new ItemStack(ModItems.VILLAGER.get());

        ModItems.VILLAGER.get().setVillager(stack, villager);

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
