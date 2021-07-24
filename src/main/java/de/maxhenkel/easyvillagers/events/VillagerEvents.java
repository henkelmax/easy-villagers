package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import net.minecraft.client.Minecraft;
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
        if (!(event.getTarget() instanceof Villager)) {
            return;
        }

        if (!Main.CLIENT_CONFIG.enableRightClickPickup.get()) {
            return;
        }

        Villager villager = (Villager) event.getTarget();
        Player player = event.getPlayer();

        if (player.level.isClientSide || !player.isShiftKeyDown()) {
            return;
        }

        if (!villager.isAlive()) {
            return;
        }

        pickUp(villager, player);

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

        if (!(pointedEntity instanceof Villager) || !pointedEntity.isAlive()) {
            return;
        }

        Main.SIMPLE_CHANNEL.sendToServer(new MessagePickUpVillager(pointedEntity.getUUID()));
    }

    public static void pickUp(Villager villager, Player player) {
        ItemStack stack = new ItemStack(ModItems.VILLAGER);

        ModItems.VILLAGER.setVillager(stack, villager);

        if (player.getInventory().add(stack)) {
            villager.discard();
        }
    }

}
