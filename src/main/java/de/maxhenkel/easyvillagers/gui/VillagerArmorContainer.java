package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VillagerArmorContainer implements Container {

    private static final EquipmentSlot[] SLOTS = new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};

    private final EasyVillagerEntity villager;
    private final Runnable onMarkDirty;


    public VillagerArmorContainer(EasyVillagerEntity villager, Runnable onMarkDirty) {
        this.villager = villager;
        this.onMarkDirty = onMarkDirty;
    }

    @Override
    public int getContainerSize() {
        return SLOTS.length;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return villager.getItemBySlot(SLOTS[i]);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack item = villager.getItemBySlot(SLOTS[index]);
        if (item.isEmpty() || count < 0) {
            return ItemStack.EMPTY;
        }
        ItemStack result = item.split(count);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack item = getItem(i);
        setItem(i, ItemStack.EMPTY);
        return item;
    }

    @Override
    public void setItem(int i, ItemStack stack) {
        villager.setItemSlot(SLOTS[i], stack);
    }

    @Override
    public void setChanged() {
        onMarkDirty.run();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }
}
