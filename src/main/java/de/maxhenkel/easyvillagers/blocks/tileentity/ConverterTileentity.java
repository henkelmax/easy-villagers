package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.merchant.IReputationType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.UUID;

public class ConverterTileentity extends VillagerTileentity implements ITickableTileEntity {

    public static final long ZOMBIFY_TIME = 20 * 3;
    public static final long CURE_TIME = ZOMBIFY_TIME + 20 * 3;
    //public static final long CONVERT_TIME = CURE_TIME + 20 * 60 * 5; //TODO
    public static final long CONVERT_TIME = CURE_TIME + 20 * 10;
    public static final long FINALIZE_TIME = CONVERT_TIME + 20 * 3;

    private NonNullList<ItemStack> inputInventory;
    private NonNullList<ItemStack> outputInventory;

    private long timer;
    private UUID owner;

    public ConverterTileentity() {
        super(ModTileEntities.CONVERTER);
        inputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            return;
        }

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
            if (timer == ZOMBIFY_TIME) {
                TraderBlock.playVillagerSound(world, pos, SoundEvents.ENTITY_ZOMBIE_INFECT);
                sync();
            } else if (timer == CURE_TIME) {
                TraderBlock.playVillagerSound(world, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE);
                sync();
            } else if (timer == CONVERT_TIME) {
                TraderBlock.playVillagerSound(world, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED);
                sync();
            } else if (timer >= FINALIZE_TIME) {
                PlayerEntity ownerPlayer = getOwnerPlayer();
                if (ownerPlayer != null) {
                    for (int i = 0; i < outputInventory.size(); i++) {
                        ItemStack stack = outputInventory.get(i);
                        if (stack.isEmpty()) {
                            VillagerEntity villagerEntity = getVillagerEntity();
                            villagerEntity.updateReputation(IReputationType.ZOMBIE_VILLAGER_CURED, ownerPlayer);
                            outputInventory.set(i, removeVillager().copy());
                            timer = 0L;
                            sync();
                            break;
                        }
                    }
                }
            }

            timer++;
            markDirty();
        } else if (timer != 0L) {
            timer = 0L;
            markDirty();
        }
        if (hasVillager() && world.getGameTime() % 20 == 0 && world.rand.nextInt(40) == 0) {
            if (timer < ZOMBIFY_TIME || timer >= CONVERT_TIME) {
                TraderBlock.playVillagerSound(world, getPos(), SoundEvents.ENTITY_VILLAGER_AMBIENT);
            } else {
                TraderBlock.playVillagerSound(world, getPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT);
            }
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
        return PotionUtils.getEffectsFromStack(stack).stream().anyMatch(effectInstance -> effectInstance.getPotion().equals(Effects.WEAKNESS));
    }

    public long getTimer() {
        return timer;
    }

    public UUID getOwner() {
        return owner;
    }

    public PlayerEntity getOwnerPlayer() {
        if (owner == null) {
            return null;
        }
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            return serverWorld.getServer().getPlayerList().getPlayerByUUID(owner);
        } else {
            return world.getPlayerByUuid(owner);
        }
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("InputInventory", ItemStackHelper.saveAllItems(new CompoundNBT(), inputInventory, true));
        compound.put("OutputInventory", ItemStackHelper.saveAllItems(new CompoundNBT(), outputInventory, true));
        compound.putLong("Timer", timer);
        if (owner != null) {
            compound.putUniqueId("Owner", owner);
        }
        return super.write(compound);
    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        ItemStackHelper.loadAllItems(compound.getCompound("InputInventory"), inputInventory);
        ItemStackHelper.loadAllItems(compound.getCompound("OutputInventory"), outputInventory);
        timer = compound.getLong("Timer");
        if (compound.contains("Owner")) {
            owner = compound.getUniqueId("Owner");
        } else {
            owner = null;
        }
        super.func_230337_a_(state, compound);
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
            if (side.equals(Direction.DOWN)) {
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
