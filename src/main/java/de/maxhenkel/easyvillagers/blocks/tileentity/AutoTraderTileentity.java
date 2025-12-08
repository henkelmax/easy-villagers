package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.ITickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.inventory.InputOnlyResourceHandler;
import de.maxhenkel.easyvillagers.inventory.ListAccessItemStacksResourceHandler;
import de.maxhenkel.easyvillagers.inventory.OutputOnlyResourceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nullable;

public class AutoTraderTileentity extends TraderTileentityBase implements ITickableBlockEntity {

    protected Container tradeGuiInv;

    protected final ListAccessItemStacksResourceHandler inputInventory;
    protected final ListAccessItemStacksResourceHandler outputInventory;

    protected int tradeIndex;
    protected CombinedResourceHandler<ItemResource> itemHandler;

    public AutoTraderTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.AUTO_TRADER.get(), ModBlocks.AUTO_TRADER.get().defaultBlockState(), pos, state);
        tradeGuiInv = new SimpleContainer(3);

        inputInventory = new ListAccessItemStacksResourceHandler(4);
        outputInventory = new ListAccessItemStacksResourceHandler(4);

        itemHandler = new CombinedResourceHandler<>(new InputOnlyResourceHandler(inputInventory), new OutputOnlyResourceHandler(outputInventory));
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
        if (offer == null || offer.isOutOfStock() || ResourceHandlerUtil.isEmpty(inputInventory)) {
            return;
        }


        try (Transaction transaction = Transaction.open(null)) {
            if (!removeNeededItems(getAutoTradeInputA(), transaction)) {
                return;
            }
            if (!removeNeededItems(offer.getCostB(), transaction)) {
                return;
            }
            if (!insertItems(offer.getResult(), transaction)) {
                return;
            }
            transaction.commit();
        }

        Villager villager = getVillagerEntity();
        offer.increaseUses();
        villager.setVillagerXp(villager.getVillagerXp() + offer.getXp());
        if (villager.shouldIncreaseLevel()) {
            villager.increaseProfessionLevelOnUpdate = true;
        }

        setChanged();
    }

    protected boolean removeNeededItems(ItemStack buying, TransactionContext transaction) {
        if (buying.isEmpty()) {
            return true;
        }
        int extract = inputInventory.extract(ItemResource.of(buying), buying.getCount(), transaction);
        return extract >= buying.getCount();
    }

    protected boolean insertItems(ItemStack insert, TransactionContext transaction) {
        if (insert.isEmpty()) {
            return true;
        }
        return outputInventory.insert(ItemResource.of(insert), insert.getCount(), transaction) >= insert.getCount();
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
        return EasyVillagersMod.SERVER_CONFIG.autoTraderMinRestockTime.get() + level.random.nextInt(Math.max(EasyVillagersMod.SERVER_CONFIG.autoTraderMaxRestockTime.get() - EasyVillagersMod.SERVER_CONFIG.autoTraderMinRestockTime.get(), 1));
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        valueOutput.putInt("Trade", tradeIndex);
        ItemUtils.saveInventory(valueOutput.child("InputInventory"), "Items", inputInventory.getRaw());
        ItemUtils.saveInventory(valueOutput.child("OutputInventory"), "Items", outputInventory.getRaw());
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        tradeIndex = valueInput.getIntOr("Trade", 0);

        ItemUtils.readInventory(valueInput.childOrEmpty("InputInventory"), "Items", inputInventory.getRaw());
        ItemUtils.readInventory(valueInput.childOrEmpty("OutputInventory"), "Items", outputInventory.getRaw());
    }

    public Container getInputInventory() {
        return new ItemListInventory(inputInventory.getRaw(), this::setChanged);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory.getRaw(), this::setChanged);
    }

    public CombinedResourceHandler<ItemResource> getItemHandler() {
        return itemHandler;
    }

}
