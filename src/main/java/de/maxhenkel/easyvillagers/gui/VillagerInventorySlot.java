package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VillagerInventorySlot extends Slot {

    protected final InventoryViewerTileentity inventoryViewer;

    public VillagerInventorySlot(Container c, int index, int xPos, int yPos, InventoryViewerTileentity inventoryViewer) {
        super(c, index, xPos, yPos);
        this.inventoryViewer = inventoryViewer;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        EasyVillagerEntity v = inventoryViewer.getVillagerEntity();
        if (v == null) {
            return false;
        }
        return super.mayPlace(stack) && v.wantsToPickUp(stack);
    }

}
