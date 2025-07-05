package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import static de.maxhenkel.easyvillagers.datacomponents.VillagerData.getCacheVillager;

public class ClientVillagerItemUtils {

    @Nullable
    public static Component getClientName(ItemStack stack) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            EasyVillagerEntity villager = getCacheVillager(stack, world);
            if (!villager.hasCustomName() && villager.isBaby()) {
                return Component.translatable("item.easy_villagers.baby_villager");
            }
            return villager.getDisplayName();
        }
        return null;
    }

}
