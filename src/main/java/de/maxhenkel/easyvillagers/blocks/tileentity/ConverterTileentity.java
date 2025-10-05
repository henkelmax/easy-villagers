package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.VillagerConvertSlot;
import de.maxhenkel.easyvillagers.inventory.ListAccessItemStacksResourceHandler;
import de.maxhenkel.easyvillagers.inventory.OutputOnlyResourceHandler;
import de.maxhenkel.easyvillagers.inventory.ValidateResourceHandler;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.UUID;

public class ConverterTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    protected ValidateResourceHandler inputInventory;
    protected ListAccessItemStacksResourceHandler outputInventory;

    protected long timer;
    protected UUID owner;

    protected CombinedResourceHandler<ItemResource> itemHandler;

    public ConverterTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.CONVERTER.get(), ModBlocks.CONVERTER.get().defaultBlockState(), pos, state);
        inputInventory = new ValidateResourceHandler(4, VillagerConvertSlot::isValid);
        outputInventory = new ListAccessItemStacksResourceHandler(4);
        itemHandler = new CombinedResourceHandler<>(new OutputOnlyResourceHandler(inputInventory), new OutputOnlyResourceHandler(outputInventory));
    }

    @Override
    public void tickServer() {
        if (timer <= 0L && !hasVillager()) {
            try (Transaction transaction = Transaction.open(null)) {
                if (consumeConvertItems(transaction)) {
                    for (int i = 0; i < inputInventory.size(); i++) {
                        ItemResource resource = inputInventory.getResource(i);
                        if (!(resource.getItem() instanceof VillagerItem)) {
                            continue;
                        }
                        if (inputInventory.extract(resource, 1, transaction) > 0) {
                            ItemStack copy = resource.toStack();
                            copy.setCount(1);
                            setVillager(copy);
                            sync();
                            transaction.commit();
                            break;
                        }
                    }
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
                        ItemResource stack = outputInventory.getResource(i);
                        if (stack.isEmpty()) {
                            EasyVillagerEntity villagerEntity = getVillagerEntity();
                            villagerEntity.onReputationEventFrom(ReputationEventType.ZOMBIE_VILLAGER_CURED, ownerPlayer);
                            outputInventory.set(i, ItemResource.of(removeVillager().copy()), 1);
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

    private boolean consumeConvertItems(TransactionContext transaction) {
        ItemResource appleStack = null;
        ItemResource potionStack = null;
        for (int i = 0; i < inputInventory.size(); i++) {
            ItemResource resource = inputInventory.getResource(i);
            if (resource.getItem() == Items.GOLDEN_APPLE) {
                appleStack = resource;
            }
            if (isWeakness(resource)) {
                potionStack = resource;
            }
        }

        if (appleStack == null || potionStack == null) {
            return false;
        }
        if (inputInventory.extract(appleStack, 1, transaction) <= 0) {
            return false;
        }
        if (inputInventory.extract(potionStack, 1, transaction) <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isWeakness(ItemResource stack) {
        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
        if (potionContents == null) {
            return false;
        }
        return potionContents.potion().filter(potionHolder -> potionHolder.equals(Potions.WEAKNESS) || potionHolder.equals(Potions.LONG_WEAKNESS)).isPresent();
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
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        ItemUtils.saveInventory(valueOutput.child("InputInventory"), "Items", inputInventory.getRaw());
        ItemUtils.saveInventory(valueOutput.child("OutputInventory"), "Items", outputInventory.getRaw());

        valueOutput.putLong("Timer", timer);
        if (owner != null) {
            valueOutput.store("Owner", UUIDUtil.CODEC, owner);
        }
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        ItemUtils.readInventory(valueInput.childOrEmpty("InputInventory"), "Items", inputInventory.getRaw());
        ItemUtils.readInventory(valueInput.childOrEmpty("OutputInventory"), "Items", outputInventory.getRaw());

        timer = valueInput.getLongOr("Timer", 0L);
        owner = valueInput.read("Owner", UUIDUtil.CODEC).orElse(null);
        super.loadAdditional(valueInput);
    }

    public Container getInputInventory() {
        return new ItemListInventory(inputInventory.getRaw(), this::setChanged);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory.getRaw(), this::setChanged);
    }

    public static int getZombifyTime() {
        return 20 * 3;
    }

    public static int getCureTime() {
        return getZombifyTime() + 20 * 3;
    }

    public static int getConvertTime() {
        return getCureTime() + EasyVillagersMod.SERVER_CONFIG.convertingTime.get();
    }

    public static int getFinalizeTime() {
        return getConvertTime() + 20 * 3;
    }

    public ResourceHandler<ItemResource> getItemHandler() {
        return itemHandler;
    }

}
