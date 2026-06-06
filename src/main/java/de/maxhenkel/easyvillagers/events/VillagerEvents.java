package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.EasyVillagersClientMod;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VillagerEvents {

    public static void clientSetup() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClientSide()) {
                return InteractionResult.PASS;
            }
            if (!(entity instanceof Villager villager)) {
                return InteractionResult.PASS;
            }
            if (!EasyVillagersMod.CONFIG.client.enableRightClickPickup.get()) {
                return InteractionResult.PASS;
            }
            if (!player.isShiftKeyDown()) {
                return InteractionResult.PASS;
            }
            if (!arePickupConditionsMet(villager)) {
                return InteractionResult.PASS;
            }
            
            ClientPlayNetworking.send(new MessagePickUpVillager(villager.getUUID()));
            return InteractionResult.SUCCESS;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (EasyVillagersClientMod.PICKUP_KEY.consumeClick()) {
                Entity pointedEntity = client.crosshairPickEntity;
                if (!(pointedEntity instanceof Villager villager) || !arePickupConditionsMet(villager)) {
                    continue;
                }
                ClientPlayNetworking.send(new MessagePickUpVillager(villager.getUUID()));
            }
        });
    }

    public static void pickUp(Villager villager, Player player) {
        if (!arePickupConditionsMet(villager)) {
            return;
        }

        ItemStack stack = new ItemStack(ModItems.VILLAGER);
        VillagerData.applyToItem(stack, villager);

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
