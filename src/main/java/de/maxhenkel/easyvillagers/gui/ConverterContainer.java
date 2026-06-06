package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;

public class ConverterContainer extends InputOutputContainer {

    public ConverterContainer(int id, Inventory playerInventory, Container inputInventory, Container outputInventory, ContainerLevelAccess access) {
        super(Containers.CONVERTER_CONTAINER, id, playerInventory, inputInventory, outputInventory, access);
    }

        public ConverterContainer(int id, Inventory playerInventory) {
        super(Containers.CONVERTER_CONTAINER, id, playerInventory);
    }

    public ConverterContainer(int id, Inventory playerInventory, net.minecraft.core.BlockPos pos) {
        super(Containers.CONVERTER_CONTAINER, id, playerInventory, pos);
    }

    @Override
    public Slot getInputSlot(Container inventory, int id, int x, int y) {
        return new VillagerConvertSlot(inventory, id, x, y);
    }

    @Override
    public Block getBlock() {
        return ModBlocks.CONVERTER;
    }
}
