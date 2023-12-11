package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class VillagerItemStackHandler extends ItemStackHandler {

    protected final InventoryViewerTileentity inventoryViewer;

    public VillagerItemStackHandler(NonNullList<ItemStack> stacks, InventoryViewerTileentity inventoryViewer) {
        super(stacks);
        this.inventoryViewer = inventoryViewer;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        EasyVillagerEntity v = inventoryViewer.getVillagerEntity();
        return super.isItemValid(slot, stack) && v != null && v.wantsToPickUp(stack);
    }

}
