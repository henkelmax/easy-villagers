package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.ITickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class AutoTraderTileentity extends TraderTileentityBase implements ITickableBlockEntity {

    protected Container tradeGuiInv;

    private final NonNullList<ItemStack> inputInventory;
    private final NonNullList<ItemStack> outputInventory;

    protected int tradeIndex;

    public AutoTraderTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.AUTO_TRADER, ModBlocks.AUTO_TRADER.defaultBlockState(), pos, state);
        tradeGuiInv = new SimpleContainer(3);

        inputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (!hasVillager()) {
            return;
        }

        if (level.getGameTime() % Main.SERVER_CONFIG.autoTraderCooldown.get() != 0) {
            return;
        }

        MerchantOffer offer = getOffer();
        if (offer == null || offer.isOutOfStock() || inputInventory.isEmpty()) {
            return;
        }

        Villager villager = getVillagerEntity();

        if (!hasEnoughItems(offer.getCostA(), false)
                || !hasEnoughItems(offer.getCostB(), false)
                || !canFitItems(offer.getResult(), false)) {
            return;
        }

        hasEnoughItems(offer.getCostA(), true);
        hasEnoughItems(offer.getCostB(), true);
        canFitItems(offer.getResult(), true);

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
        IItemHandlerModifiable itemHandler = getOutputInventoryItemHandler();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            stack = itemHandler.insertItem(i, stack, !doInsert);
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
        tradeGuiInv.setItem(0, offer.getCostA());
        tradeGuiInv.setItem(1, offer.getCostB());
        tradeGuiInv.setItem(2, offer.getResult());
    }

    @Nullable
    public MerchantOffer getOffer() {
        Villager villagerEntity = getVillagerEntity();
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
        Villager villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return 0;
        }
        return villagerEntity.getOffers().size();
    }

    @Override
    protected long calculateNextRestock() {
        return Main.SERVER_CONFIG.autoTraderMinRestockTime.get() + level.random.nextInt(Math.max(Main.SERVER_CONFIG.autoTraderMaxRestockTime.get() - Main.SERVER_CONFIG.autoTraderMinRestockTime.get(), 1));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("Trade", tradeIndex);
        compound.put("InputInventory", ContainerHelper.saveAllItems(new CompoundTag(), inputInventory, true));
        compound.put("OutputInventory", ContainerHelper.saveAllItems(new CompoundTag(), outputInventory, true));
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        tradeIndex = compound.getInt("Trade");
        ContainerHelper.loadAllItems(compound.getCompound("InputInventory"), inputInventory);
        ContainerHelper.loadAllItems(compound.getCompound("OutputInventory"), outputInventory);
    }

    public Container getInputInventory() {
        return new ItemListInventory(inputInventory, this::setChanged);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory, this::setChanged);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
