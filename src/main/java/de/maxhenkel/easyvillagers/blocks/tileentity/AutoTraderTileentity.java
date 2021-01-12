package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class AutoTraderTileentity extends TraderTileentityBase {

    protected IInventory tradeGuiInv;

    private final NonNullList<ItemStack> inputInventory;
    private final NonNullList<ItemStack> outputInventory;

    protected int tradeIndex;

    public AutoTraderTileentity() {
        super(ModTileEntities.AUTO_TRADER, ModBlocks.AUTO_TRADER.getDefaultState());
        tradeGuiInv = new Inventory(3);

        inputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();

        if (!hasVillager()) {
            return;
        }

        if (world.getGameTime() % Main.SERVER_CONFIG.autoTraderCooldown.get() != 0) {
            return;
        }

        MerchantOffer offer = getOffer();
        if (offer == null || offer.hasNoUsesLeft() || inputInventory.isEmpty()) {
            return;
        }

        VillagerEntity villager = getVillagerEntity();

        if (!hasEnoughItems(offer.func_222205_b(), false)
                || !hasEnoughItems(offer.getBuyingStackSecond(), false)
                || !canFitItems(offer.getSellingStack(), false)) {
            return;
        }

        hasEnoughItems(offer.func_222205_b(), true);
        hasEnoughItems(offer.getBuyingStackSecond(), true);
        canFitItems(offer.getSellingStack(), true);

        // villager.onTrade(offer) spawns XP, so we manually do all necessary stuff
        offer.increaseUses();
        try {
            villager.setXp(villager.getXp() + offer.getGivenExp());
            if ((boolean) CAN_LEVEL_UP.invoke(villager)) {
                LEVELED_UP.set(villager, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        markDirty();
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
        IItemHandlerModifiable itemHandler = getOutputInventoryItemHandler();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            stack = itemHandler.insertItem(i, stack, !doInsert);
            if (stack.isEmpty()) {
                break;
            }
        }

        return stack.isEmpty();
    }

    public IInventory getTradeGuiInv() {
        updateTradeInv();
        return tradeGuiInv;
    }

    public int getTradeIndex() {
        return tradeIndex;
    }

    public void setTradeIndex(int tradeIndex) {
        this.tradeIndex = tradeIndex;
        updateTradeInv();
        markDirty();
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
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            tradeGuiInv.clear();
            return;
        }
        villagerEntity.recalculateOffers();
        MerchantOffer offer = getOffer();
        if (offer == null) {
            tradeGuiInv.clear();
            return;
        }
        tradeGuiInv.setInventorySlotContents(0, offer.func_222205_b());
        tradeGuiInv.setInventorySlotContents(1, offer.getBuyingStackSecond());
        tradeGuiInv.setInventorySlotContents(2, offer.getSellingStack());
    }

    @Nullable
    public MerchantOffer getOffer() {
        VillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return null;
        }
        MerchantOffers offers = villagerEntity.getOffers();
        if (tradeIndex < 0 || tradeIndex >= offers.size()) {
            return null;
        }
        return offers.get(tradeIndex);
    }

    protected int getTradeCount() {
        VillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return 0;
        }
        return villagerEntity.getOffers().size();
    }

    @Override
    protected long calculateNextRestock() {
        return Main.SERVER_CONFIG.autoTraderMinRestockTime.get() + world.rand.nextInt(Math.max(Main.SERVER_CONFIG.autoTraderMaxRestockTime.get() - Main.SERVER_CONFIG.autoTraderMinRestockTime.get(), 1));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Trade", tradeIndex);
        compound.put("InputInventory", ItemStackHelper.saveAllItems(new CompoundNBT(), inputInventory, true));
        compound.put("OutputInventory", ItemStackHelper.saveAllItems(new CompoundNBT(), outputInventory, true));
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        tradeIndex = compound.getInt("Trade");
        ItemStackHelper.loadAllItems(compound.getCompound("InputInventory"), inputInventory);
        ItemStackHelper.loadAllItems(compound.getCompound("OutputInventory"), outputInventory);
    }

    public IInventory getInputInventory() {
        return new ItemListInventory(inputInventory, this::markDirty);
    }

    public IInventory getOutputInventory() {
        return new ItemListInventory(outputInventory, this::markDirty);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side != null && side.equals(Direction.DOWN)) {
                return LazyOptional.of(this::getOutputInventoryItemHandler).cast();
            } else {
                return LazyOptional.of(this::getInputInventoryItemHandler).cast();
            }

        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable foodInventoryHandler;

    public IItemHandlerModifiable getInputInventoryItemHandler() {
        if (foodInventoryHandler == null) {
            foodInventoryHandler = new ItemStackHandler(inputInventory);
        }
        return foodInventoryHandler;
    }

    private IItemHandlerModifiable outputInventoryHandler;

    public IItemHandlerModifiable getOutputInventoryItemHandler() {
        if (outputInventoryHandler == null) {
            outputInventoryHandler = new ItemStackHandler(outputInventory);
        }
        return outputInventoryHandler;
    }

}
