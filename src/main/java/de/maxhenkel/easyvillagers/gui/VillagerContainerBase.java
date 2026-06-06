package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.gui.ContainerBase;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;

public abstract class VillagerContainerBase extends ContainerBase {

    protected ContainerLevelAccess access;

    @SuppressWarnings("this-escape")
    public VillagerContainerBase(MenuType<?> containerType, int id, Container playerInventory, Container inventory, ContainerLevelAccess access) {
        super(containerType, id, playerInventory, inventory);
        this.access = access;
    }

    public abstract Block getBlock();

    public net.minecraft.world.level.block.entity.BlockEntity getBlockEntity() {
        return access.evaluate((level, pos) -> level.getBlockEntity(pos)).orElse(null);
    }

    @Override
    public boolean stillValid(Player player) {
        return super.stillValid(player) && AbstractContainerMenu.stillValid(access, player, getBlock());
    }
}
