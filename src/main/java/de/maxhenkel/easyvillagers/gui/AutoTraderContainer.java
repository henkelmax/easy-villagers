package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.gui.LockedSlot;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;

public class AutoTraderContainer extends VillagerContainerBase {

    private final AutoTraderTileentity trader;
    private boolean locked;

    @SuppressWarnings("this-escape")
    public AutoTraderContainer(int id, Inventory playerInventory, Container tradeSlots, AutoTraderTileentity trader, Container inputItems, Container outputItems, ContainerLevelAccess access) {
        super(Containers.AUTO_TRADER_CONTAINER, id, playerInventory, null, access);
        this.trader = trader;
        addSlot(new LockedSlot(tradeSlots, 0, 36, 21, false, false));
        addSlot(new LockedSlot(tradeSlots, 1, 62, 21, false, false));
        addSlot(new LockedSlot(tradeSlots, 2, 120, 22, false, false));

        for (int i = 0; i < 4; i++) {
            addSlot(new Slot(inputItems, i, 53 + i * 18, 57));
        }

        for (int i = 0; i < 4; i++) {
            addSlot(new LockedSlot(outputItems, i, 53 + i * 18, 88, true, false));
        }

        addPlayerInventorySlots();

        addDataSlots(FIELDS);
    }

    public AutoTraderContainer(int id, Inventory playerInventory, AutoTraderTileentity trader, Container inputItems, Container outputItems, ContainerLevelAccess access) {
        this(id, playerInventory, trader.getTradeGuiInv(), trader, inputItems, outputItems, access);
    }

    public AutoTraderContainer(int id, Inventory playerInventory, net.minecraft.core.BlockPos pos) {
        this(id, playerInventory, get(pos));
    }

    public AutoTraderContainer(int id, Inventory playerInventory, AutoTraderTileentity te) {
        this(id, playerInventory, te, te.getInputInventory(), te.getOutputInventory(), ContainerLevelAccess.create(te.getLevel(), te.getBlockPos()));
    }

    private static AutoTraderTileentity get(net.minecraft.core.BlockPos pos) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level == null) {
            throw new IllegalStateException("No client level present");
        }
        if (!(level.getBlockEntity(pos) instanceof AutoTraderTileentity trader)) {
            throw new IllegalStateException("Could not find block entity");
        }
        return trader;
    }

    public final ContainerData FIELDS = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == 0) {
                MerchantOffer offer = trader.getOffer();
                if (offer == null) {
                    return 1;
                }
                return offer.isOutOfStock() ? 0 : 1;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                locked = value == 0;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public boolean isLocked() {
        return locked;
    }

    public int getInvOffset() {
        return 36;
    }

    public int getInventorySize() {
        return 11;
    }

    public AutoTraderTileentity getTrader() {
        return trader;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < getInventorySize()) {
                if (!moveItemStackTo(stack, getInventorySize(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, 3, getInventorySize(), false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public Block getBlock() {
        return ModBlocks.AUTO_TRADER;
    }
}
