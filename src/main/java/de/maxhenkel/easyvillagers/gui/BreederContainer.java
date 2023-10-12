package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;

public class BreederContainer extends InputOutputContainer {

    public BreederContainer(int id, Inventory playerInventory, Container foodInventory, Container outputInventory, ContainerLevelAccess access) {
        super(Containers.BREEDER_CONTAINER.get(), id, playerInventory, foodInventory, outputInventory, access);
    }

    public BreederContainer(int id, Inventory playerInventory) {
        super(Containers.BREEDER_CONTAINER.get(), id, playerInventory);
    }

    @Override
    public Slot getInputSlot(Container inventory, int id, int x, int y) {
        return new FoodSlot(inventory, id, x, y);
    }

    @Override
    public Block getBlock() {
        return ModBlocks.BREEDER.get();
    }
}
