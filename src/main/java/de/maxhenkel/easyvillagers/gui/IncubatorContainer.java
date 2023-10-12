package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;

public class IncubatorContainer extends InputOutputContainer {

    public IncubatorContainer(int id, Inventory playerInventory, Container inputInventory, Container outputInventory, ContainerLevelAccess access) {
        super(Containers.INCUBATOR_CONTAINER.get(), id, playerInventory, inputInventory, outputInventory, access);
    }

    public IncubatorContainer(int id, Inventory playerInventory) {
        super(Containers.INCUBATOR_CONTAINER.get(), id, playerInventory);
    }

    @Override
    public Slot getInputSlot(Container inventory, int id, int x, int y) {
        return new VillagerIncubateSlot(inventory, id, x, y);
    }

    @Override
    public Block getBlock() {
        return ModBlocks.INCUBATOR.get();
    }
}
