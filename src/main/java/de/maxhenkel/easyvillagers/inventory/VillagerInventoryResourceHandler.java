package de.maxhenkel.easyvillagers.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class VillagerInventoryResourceHandler implements ResourceHandler<ItemResource> {

    protected Villager villager;

    public VillagerInventoryResourceHandler(Villager villager) {
        this.villager = villager;
    }

    protected NonNullList<ItemStack> getInventory() {
        return villager.getInventory().getItems();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public ItemResource getResource(int index) {
        return null;
    }

    @Override
    public long getAmountAsLong(int index) {
        return 0;
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return 0;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return false;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }
}
