package de.maxhenkel.easyvillagers.events;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
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

        if (!villager.isAlive() || villager.getLeashed()) {
            event.setCancellationResult(ActionResultType.FAIL);
            event.setCanceled(true);
            return;
        }

        pickUp(villager, player);

        event.setCancellationResult(ActionResultType.SUCCESS);
        event.setCanceled(true);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Main.PICKUP_KEY.isPressed()) {
            return;
        }

        Entity pointedEntity = Minecraft.getInstance().pointedEntity;

        if (!(pointedEntity instanceof VillagerEntity) || !pointedEntity.isAlive()) {
            return;
        }

        Main.SIMPLE_CHANNEL.sendToServer(new MessagePickUpVillager(pointedEntity.getUniqueID()));
    }

    public static void pickUp(VillagerEntity villager, PlayerEntity player) {
        ItemStack stack = new ItemStack(ModItems.VILLAGER);

        ModItems.VILLAGER.setVillager(stack, villager);

        if (player.inventory.addItemStackToInventory(stack)) {
            villager.remove();
        }
    }

}
