package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.tileentity.IServerTickableBlockEntity;
import de.maxhenkel.easyvillagers.inventory.SimpleInventory;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


import org.jetbrains.annotations.Nullable;

public class AutoTraderTileentity extends TraderTileentityBase implements IServerTickableBlockEntity {

    protected Container tradeGuiInv;

    protected final SimpleInventory inputInventory;
    protected final SimpleInventory outputInventory;

    protected int tradeIndex;

    @SuppressWarnings("this-escape")
    public AutoTraderTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.AUTO_TRADER, ModBlocks.AUTO_TRADER.defaultBlockState(), pos, state);
        tradeGuiInv = new SimpleContainer(3);

        inputInventory = new SimpleInventory(4, this::setChanged);
        outputInventory = new SimpleInventory(4, this::setChanged);
    }

    @Override
    public void tickServer() {
        super.tickServer();
        if (!hasVillager()) {
            return;
        }

        if (level.getGameTime() % EasyVillagersMod.CONFIG.server.autoTraderCooldown.get() != 0) {
            return;
        }

        MerchantOffer offer = getOffer();
        if (offer == null || offer.isOutOfStock() || inputInventory.isEmpty()) {
            return;
        }


        if (!hasNeededItems(getAutoTradeInputA()) || !hasNeededItems(offer.getCostB()) || !canInsertItems(offer.getResult())) {
            return;
        }

        removeNeededItems(getAutoTradeInputA());
        removeNeededItems(offer.getCostB());
        insertItems(offer.getResult());

        Villager villager = getVillagerEntity();
        offer.increaseUses();
        villager.setVillagerXp(villager.getVillagerXp() + offer.getXp());
        if (villager.shouldIncreaseLevel()) {
            villager.increaseProfessionLevelOnUpdate = true;
        }

        setChanged();
    }

    protected boolean hasNeededItems(ItemStack buying) {
        if (buying.isEmpty()) {
            return true;
        }
        int remaining = buying.getCount();
        for (int i = 0; i < inputInventory.getContainerSize(); i++) {
            ItemStack stack = inputInventory.getItem(i);
            if (ItemStack.isSameItemSameComponents(stack, buying)) {
                remaining -= stack.getCount();
                if (remaining <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void removeNeededItems(ItemStack buying) {
        if (buying.isEmpty()) {
            return;
        }
        int remaining = buying.getCount();
        for (int i = 0; i < inputInventory.getContainerSize(); i++) {
            ItemStack stack = inputInventory.getItem(i);
            if (ItemStack.isSameItemSameComponents(stack, buying)) {
                int toRemove = Math.min(remaining, stack.getCount());
                inputInventory.removeItem(i, toRemove);
                remaining -= toRemove;
                if (remaining <= 0) {
                    return;
                }
            }
        }
    }

    protected boolean canInsertItems(ItemStack insert) {
        if (insert.isEmpty()) {
            return true;
        }
        int remaining = insert.getCount();
        for (int i = 0; i < outputInventory.getContainerSize(); i++) {
            ItemStack stack = outputInventory.getItem(i);
            if (stack.isEmpty()) {
                return true;
            } else if (ItemStack.isSameItemSameComponents(stack, insert)) {
                remaining -= stack.getMaxStackSize() - stack.getCount();
                if (remaining <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void insertItems(ItemStack insert) {
        if (insert.isEmpty()) {
            return;
        }
        int remaining = insert.getCount();
        for (int i = 0; i < outputInventory.getContainerSize(); i++) {
            ItemStack stack = outputInventory.getItem(i);
            if (stack.isEmpty()) {
                ItemStack copy = insert.copy();
                copy.setCount(remaining);
                outputInventory.setItem(i, copy);
                return;
            } else if (ItemStack.isSameItemSameComponents(stack, insert)) {
                int toAdd = Math.min(remaining, stack.getMaxStackSize() - stack.getCount());
                stack.grow(toAdd);
                remaining -= toAdd;
                if (remaining <= 0) {
                    return;
                }
            }
        }
    }

    public Container getTradeGuiInv() {
        updateTradeInv();
        return tradeGuiInv;
    }

    public int getTradeIndex() {
        return tradeIndex;
    }

    public void setTradeIndex(int tradeIndex) {
        this.tradeIndex = tradeIndex;
        updateTradeInv();
        setChanged();
    }

    @Override
    protected void onAddVillager(EasyVillagerEntity villager) {
        super.onAddVillager(villager);
        updateTradeInv();
    }

    @Override
    public void setWorkstation(Block workstation) {
        super.setWorkstation(workstation);
        updateTradeInv();
    }

    public void nextTrade() {
        int tradeCount = getTradeCount();
        if (tradeCount > 0) {
            setTradeIndex(Math.floorMod(tradeIndex + 1, tradeCount));
        }
    }

    public void prevTrade() {
        int tradeCount = getTradeCount();
        if (tradeCount > 0) {
            setTradeIndex(Math.floorMod(tradeIndex - 1, tradeCount));
        }
    }

    protected void updateTradeInv() {
        if (level == null || level.isClientSide()) {
            return;
        }
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            tradeGuiInv.clearContent();
            return;
        }
        villagerEntity.recalculateOffers();
        MerchantOffer offer = getOffer();
        if (offer == null) {
            tradeGuiInv.clearContent();
            return;
        }
        tradeGuiInv.setItem(0, getAutoTradeInputA());
        tradeGuiInv.setItem(1, offer.getCostB());
        tradeGuiInv.setItem(2, offer.getResult());
    }

    public ItemStack getAutoTradeInputA() {
        MerchantOffer offer = getOffer();
        if (offer == null) {
            return ItemStack.EMPTY;
        }
        ItemStack costA = offer.getCostA().copy();
        int amount = Math.min(costA.getCount(), offer.getBaseCostA().getCount());
        costA.setCount(amount);
        return costA;
    }

    @Nullable
    public MerchantOffer getOffer() {
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return null;
        }
        if (villagerEntity.level().isClientSide()) {
            return null;
        }
        MerchantOffers offers = villagerEntity.getOffers();
        if (tradeIndex < 0 || tradeIndex >= offers.size()) {
            return null;
        }
        return offers.get(tradeIndex);
    }

    protected int getTradeCount() {
        Villager villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return 0;
        }
        return villagerEntity.getOffers().size();
    }

    @Override
    protected long calculateNextRestock() {
        return EasyVillagersMod.CONFIG.server.autoTraderMinRestockTime.get() + level.getRandom().nextInt(Math.max(EasyVillagersMod.CONFIG.server.autoTraderMaxRestockTime.get() - EasyVillagersMod.CONFIG.server.autoTraderMinRestockTime.get(), 1));
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        valueOutput.putInt("Trade", tradeIndex);
        ContainerHelper.saveAllItems(valueOutput.child("InputInventory"), inputInventory.getItems());
        ContainerHelper.saveAllItems(valueOutput.child("OutputInventory"), outputInventory.getItems());
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        tradeIndex = valueInput.getIntOr("Trade", 0);

        valueInput.child("InputInventory").ifPresent(input -> ContainerHelper.loadAllItems(input, inputInventory.getItems()));
        valueInput.child("OutputInventory").ifPresent(input -> ContainerHelper.loadAllItems(input, outputInventory.getItems()));
    }

    public Container getInputInventory() {
        return inputInventory;
    }

    public Container getOutputInventory() {
        return outputInventory;
    }

}
