package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.UUID;

public class ConverterTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    private NonNullList<ItemStack> inputInventory;
    private NonNullList<ItemStack> outputInventory;

    private long timer;
    private UUID owner;

    public ConverterTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.CONVERTER, ModBlocks.CONVERTER.defaultBlockState(), pos, state);
        inputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void tickServer() {
        if (timer <= 0L && !hasVillager()) {
            for (ItemStack stack : inputInventory) {
                if (stack.getItem() instanceof VillagerItem && consumeConvertItems()) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    setVillager(copy);
                    stack.shrink(1);
                    sync();
                    break;
                }
            }
        }
        if (hasVillager()) {
            if (advanceAge()) {
                sync();
            }
            if (timer == getZombifyTime()) {
                VillagerBlockBase.playVillagerSound(level, worldPosition, SoundEvents.ZOMBIE_INFECT);
                sync();
            } else if (timer == getCureTime()) {
                VillagerBlockBase.playVillagerSound(level, worldPosition, SoundEvents.ZOMBIE_VILLAGER_CURE);
                sync();
            } else if (timer == getConvertTime()) {
                VillagerBlockBase.playVillagerSound(level, worldPosition, SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
                sync();
            } else if (timer >= getFinalizeTime()) {
                Player ownerPlayer = getOwnerPlayer();
                if (ownerPlayer != null) {
                    for (int i = 0; i < outputInventory.size(); i++) {
                        ItemStack stack = outputInventory.get(i);
                        if (stack.isEmpty()) {
                            EasyVillagerEntity villagerEntity = getVillagerEntity();
                            villagerEntity.onReputationEventFrom(ReputationEventType.ZOMBIE_VILLAGER_CURED, ownerPlayer);
                            outputInventory.set(i, removeVillager().copy());
                            timer = 0L;
                            sync();
                            break;
                        }
                    }
                }
            }

            timer++;
            setChanged();
            if (timer < getZombifyTime() || timer >= getConvertTime()) {
                VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);
            } else {
                VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.ZOMBIE_VILLAGER_AMBIENT);
            }
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.ZOMBIE_AMBIENT);
        } else if (timer != 0L) {
            timer = 0L;
            setChanged();
        }
    }

    private boolean consumeConvertItems() {
        ItemStack appleStack = null;
        ItemStack potionStack = null;
        for (ItemStack stack : inputInventory) {
            if (appleStack == null && stack.getItem() == Items.GOLDEN_APPLE) {
                appleStack = stack;
            }
            if (potionStack == null && isWeakness(stack)) {
                potionStack = stack;
            }
        }

        if (appleStack != null && potionStack != null) {
            appleStack.shrink(1);
            potionStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isWeakness(ItemStack stack) {
        return PotionUtils.getPotion(stack).equals(Potions.WEAKNESS) || PotionUtils.getPotion(stack).equals(Potions.LONG_WEAKNESS);
    }

    public long getTimer() {
        return timer;
    }

    public UUID getOwner() {
        return owner;
    }

    public Player getOwnerPlayer() {
        if (owner == null) {
            return null;
        }
        if (level instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) level;
            return serverWorld.getServer().getPlayerList().getPlayer(owner);
        } else {
            return level.getPlayerByUUID(owner);
        }
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("InputInventory", ContainerHelper.saveAllItems(new CompoundTag(), inputInventory, true));
        compound.put("OutputInventory", ContainerHelper.saveAllItems(new CompoundTag(), outputInventory, true));
        compound.putLong("Timer", timer);
        if (owner != null) {
            compound.putUUID("Owner", owner);
        }
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        ContainerHelper.loadAllItems(compound.getCompound("InputInventory"), inputInventory);
        ContainerHelper.loadAllItems(compound.getCompound("OutputInventory"), outputInventory);
        timer = compound.getLong("Timer");
        if (compound.contains("Owner")) {
            owner = compound.getUUID("Owner");
        } else {
            owner = null;
        }
        super.load(compound);
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

    public static int getZombifyTime() {
        return 20 * 3;
    }

    public static int getCureTime() {
        return getZombifyTime() + 20 * 3;
    }

    public static int getConvertTime() {
        return getCureTime() + Main.SERVER_CONFIG.convertingTime.get();
    }

    public static int getFinalizeTime() {
        return getConvertTime() + 20 * 3;
    }

}
