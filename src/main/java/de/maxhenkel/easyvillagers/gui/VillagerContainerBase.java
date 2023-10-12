package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ContainerBase;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;

public abstract class VillagerContainerBase extends ContainerBase {

    protected ContainerLevelAccess access;

    public VillagerContainerBase(MenuType containerType, int id, Container playerInventory, Container inventory, ContainerLevelAccess access) {
        super(containerType, id, playerInventory, inventory);
        this.access = access;
    }

    public abstract Block getBlock();

    @Override
    public boolean stillValid(Player player) {
        return super.stillValid(player) && AbstractContainerMenu.stillValid(access, player, getBlock());
    }
}
