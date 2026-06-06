package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.gui.LockedSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

public class OutputContainer extends VillagerContainerBase {

    @Nullable
    protected Supplier<Block> blockSupplier;

    @SuppressWarnings("this-escape")
    public OutputContainer(int id, Inventory playerInventory, Container outputInventory, ContainerLevelAccess access, Supplier<Block> blockSupplier) {
        super(Containers.OUTPUT_CONTAINER, id, playerInventory, outputInventory, access);
        this.blockSupplier = blockSupplier;

        for (int i = 0; i < 4; i++) {
            addSlot(new LockedSlot(outputInventory, i, 52 + i * 18, 20, true, false));
        }

        addPlayerInventorySlots();
    }

        public OutputContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(4), ContainerLevelAccess.NULL, null);
    }

    public OutputContainer(int id, Inventory playerInventory, net.minecraft.core.BlockPos pos) {
        this(id, playerInventory, new SimpleContainer(4), ContainerLevelAccess.create(net.minecraft.client.Minecraft.getInstance().level, pos), null);
    }

    public int getInvOffset() {
        return -33;
    }

    public int getInventorySize() {
        return 4;
    }

        @Override
    public Block getBlock() {
        if (blockSupplier != null) {
            return blockSupplier.get();
        }
        return access.evaluate((level, pos) -> level.getBlockState(pos).getBlock()).orElse(net.minecraft.world.level.block.Blocks.AIR);
    }
}
