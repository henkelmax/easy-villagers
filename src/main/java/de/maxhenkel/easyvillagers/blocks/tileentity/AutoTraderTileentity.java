package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.ITickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.MultiItemStackHandler;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class AutoTraderTileentity extends TraderTileentityBase implements ITickableBlockEntity {

    protected Container tradeGuiInv;

    protected final NonNullList<ItemStack> inputInventory;
    protected final NonNullList<ItemStack> outputInventory;

    protected int tradeIndex;
    protected ItemStackHandler outputHandler;
    protected MultiItemStackHandler itemHandler;

    public AutoTraderTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.AUTO_TRADER.get(), ModBlocks.AUTO_TRADER.get().defaultBlockState(), pos, state);
        tradeGuiInv = new SimpleContainer(3);

        inputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);

        outputHandler = new ItemStackHandler(outputInventory);
        itemHandler = new MultiItemStackHandler(inputInventory, outputInventory);
    }

    @Override
    public void tick() {
        if (!hasVillager()) {
            return;
        }

        if (level.getGameTime() % EasyVillagersMod.SERVER_CONFIG.autoTraderCooldown.get() != 0) {
            return;
        }

        MerchantOffer offer = getOffer();
        if (offer == null || offer.isOutOfStock() || inputInventory.isEmpty()) {
            return;
        }

        Villager villager = getVillagerEntity();

        ItemStack autoTradeInputA = getAutoTradeInputA();
        ItemStack costB = offer.getCostB();
        ItemStack result = offer.getResult();

        if (!hasEnoughItems(autoTradeInputA, false)
                || !hasEnoughItems(costB, false)
                || !canFitItems(result, false)) {
            return;
        }

        hasEnoughItems(autoTradeInputA, true);
        hasEnoughItems(costB, true);
        canFitItems(result, true);

        // villager.onTrade(offer) spawns XP, so we manually do all necessary stuff
        offer.increaseUses();
        villager.setVillagerXp(villager.getVillagerXp() + offer.getXp());
        if (villager.shouldIncreaseLevel()) {
            villager.increaseProfessionLevelOnUpdate = true;
        }

        setChanged();
    }

    protected boolean hasEnoughItems(ItemStack buying, boolean remove) {
        if (buying.isEmpty()) {
            return true;
        }
        int amount = buying.getCount();
        for (ItemStack stack : inputInventory) {
            if (ItemUtils.isStackable(stack, buying)) {
                int am = Math.min(amount, stack.getCount());
                if (remove) {
                    stack.shrink(am);
                }
                amount -= am;
                if (amount <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean canFitItems(ItemStack insert, boolean doInsert) {
        if (insert.isEmpty()) {
            return true;
        }
        ItemStack stack = insert.copy();

        for (int i = 0; i < outputHandler.getSlots(); i++) {
            stack = outputHandler.insertItem(i, stack, !doInsert);
            if (stack.isEmpty()) {
                break;
            }
        }

        return stack.isEmpty();
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
        if (level == null || level.isClientSide) {
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
            return ItemStack.EMPTY.copy();
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
        return EasyVillagersMod.SERVER_CONFIG.autoTraderMinRestockTime.get() + level.random.nextInt(Math.max(EasyVillagersMod.SERVER_CONFIG.autoTraderMaxRestockTime.get() - EasyVillagersMod.SERVER_CONFIG.autoTraderMinRestockTime.get(), 1));
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        valueOutput.putInt("Trade", tradeIndex);
        ItemUtils.saveInventory(valueOutput.child("InputInventory"), "Items", inputInventory);
        ItemUtils.saveInventory(valueOutput.child("OutputInventory"), "Items", outputInventory);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        tradeIndex = valueInput.getIntOr("Trade", 0);

        ItemUtils.readInventory(valueInput.childOrEmpty("InputInventory"), "Items", inputInventory);
        ItemUtils.readInventory(valueInput.childOrEmpty("OutputInventory"), "Items", outputInventory);
    }

    public Container getInputInventory() {
        return new ItemListInventory(inputInventory, this::setChanged);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory, this::setChanged);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

}
