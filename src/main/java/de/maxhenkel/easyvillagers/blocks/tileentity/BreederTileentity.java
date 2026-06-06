package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible;
import de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration;

import de.maxhenkel.easyvillagers.blocks.tileentity.IServerTickableBlockEntity;
import de.maxhenkel.easyvillagers.inventory.SimpleInventory;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.FoodSlot;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessageVillagerParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

// TODO: Replace PacketDistributor with Fabric networking
// import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

public class BreederTileentity extends FakeWorldTileentity implements IServerTickableBlockEntity, de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible {

    protected final SimpleInventory foodInventory;
    protected final SimpleInventory outputInventory;
    protected ItemStack villager1;
    protected EasyVillagerEntity villagerEntity1;
    protected ItemStack villager2;
    protected EasyVillagerEntity villagerEntity2;
    protected TRSlotConfiguration trSlotConfig;

    @SuppressWarnings("this-escape")
    public BreederTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.BREEDER, ModBlocks.BREEDER.defaultBlockState(), pos, state);
        foodInventory = new SimpleInventory(4, this::setChanged);
        outputInventory = new SimpleInventory(4, this::setChanged);
        villager1 = ItemStack.EMPTY;
        villager2 = ItemStack.EMPTY;
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
            trSlotConfig = new TRSlotConfiguration();
        }
    }

    @Override
    public Object getSlotConfiguration() {
        return trSlotConfig;
    }

    public ItemStack getVillager1() {
        return villager1;
    }

    public ItemStack getVillager2() {
        return villager2;
    }

    public boolean hasVillager1() {
        return !villager1.isEmpty();
    }

    public boolean hasVillager2() {
        return !villager2.isEmpty();
    }

    public EasyVillagerEntity getVillagerEntity1() {
        if (villagerEntity1 == null && !villager1.isEmpty()) {
            villagerEntity1 = VillagerData.createEasyVillager(villager1, level);
        }
        return villagerEntity1;
    }

    public EasyVillagerEntity getVillagerEntity2() {
        if (villagerEntity2 == null && !villager2.isEmpty()) {
            villagerEntity2 = VillagerData.createEasyVillager(villager2, level);
        }
        return villagerEntity2;
    }

    public void setVillager1(ItemStack villager) {
        this.villager1 = villager;

        if (villager.isEmpty()) {
            villagerEntity1 = null;
        } else {
            villagerEntity1 = VillagerData.createEasyVillager(villager, level);
        }
        setChanged();
        sync();
    }

    public void setVillager2(ItemStack villager) {
        this.villager2 = villager;

        if (villager.isEmpty()) {
            villagerEntity2 = null;
        } else {
            villagerEntity2 = VillagerData.createEasyVillager(villager, level);
        }
        setChanged();
        sync();
    }

    public ItemStack removeVillager1() {
        ItemStack v = villager1;
        setVillager1(ItemStack.EMPTY);
        return v;
    }

    public ItemStack removeVillager2() {
        ItemStack v = villager2;
        setVillager2(ItemStack.EMPTY);
        return v;
    }

    @Override
    public void tickServer() {
        if (level.isClientSide()) {
            return;
        }

        boolean age1 = VillagerTileentity.advanceAge(getVillagerEntity1());
        boolean age2 = VillagerTileentity.advanceAge(getVillagerEntity2());
        if (age1 || age2) {
            sync();
        }
        if (hasVillager1() || hasVillager2()) {
            setChanged();
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);
        }

        if (level.getGameTime() % EasyVillagersMod.CONFIG.server.breedingTime.get() == 0) {
            tryBreed();
        }
    }

    public void tryBreed() {
        if (!canBreed()) {
            return;
        }
        if (!removeBreedingItems()) {
            return;
        }
        if (!addVillager()) {
            return;
        }
        VillagerBlockBase.playVillagerSound(level, worldPosition, SoundEvents.VILLAGER_CELEBRATE);
        spawnParticles();
    }

    public void spawnParticles() {
        if (level instanceof ServerLevel serverLevel) {
            // TODO: Replace with Fabric networking
            // PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(worldPosition), new MessageVillagerParticles(worldPosition));

        } else if (level.isClientSide()) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ParticleTypes.HEART,
                        worldPosition.getX() + (level.getRandom().nextDouble() - 0.5D) + 0.5D,
                        worldPosition.getY() + level.getRandom().nextDouble() + 1D,
                        worldPosition.getZ() + (level.getRandom().nextDouble() - 0.5D) + 0.5D,
                        0D, 0D, 0D);
            }
        }
    }

    private boolean addVillager() {
        EasyVillagerEntity villagerEntity = new EasyVillagerEntity(EntityType.VILLAGER, level);
        villagerEntity.setVillagerData(villagerEntity.getVillagerData().withType(level.registryAccess(), VillagerType.byBiome(level.getBiome(getBlockPos()))));
        villagerEntity.setAge(-24000);
        ItemStack villager = new ItemStack(ModItems.VILLAGER);
        VillagerData.applyToItem(villager, villagerEntity);
        
        for (int i = 0; i < outputInventory.getContainerSize(); i++) {
            ItemStack stack = outputInventory.getItem(i);
            if (stack.isEmpty()) {
                outputInventory.setItem(i, villager);
                return true;
            }
        }
        return false;
    }

    public boolean canBreed() {
        if (!hasVillager1() || !hasVillager2()) {
            return false;
        }
        if (getVillagerEntity1().isBaby() || getVillagerEntity2().isBaby()) {
            return false;
        }
        int value = 0;
        for (int i = 0; i < foodInventory.getContainerSize(); i++) {
            ItemStack stack = foodInventory.getItem(i);
            value += Villager.FOOD_POINTS.getOrDefault(stack.getItem(), 0) * stack.getCount();
        }
        return value >= 24;
    }

    private boolean removeBreedingItems() {
        int value = 0;
        for (int i = 0; i < foodInventory.getContainerSize(); i++) {
            ItemStack stack = foodInventory.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            int itemValue = Villager.FOOD_POINTS.getOrDefault(stack.getItem(), 0);
            if (itemValue <= 0) {
                continue;
            }
            int amountNeeded = 24 - value;
            int amountToRemove = Math.min(stack.getCount(), (amountNeeded + itemValue - 1) / itemValue);
            
            ItemStack extracted = foodInventory.removeItem(i, amountToRemove);
            value += extracted.getCount() * itemValue;
            if (value >= 24) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        if (hasVillager1()) {
            valueOutput.store("Villager1", ItemStack.CODEC, getVillager1());
        }
        if (hasVillager2()) {
            valueOutput.store("Villager2", ItemStack.CODEC, getVillager2());
        }
        ContainerHelper.saveAllItems(valueOutput.child("FoodInventory"), foodInventory.getItems());
        
        ContainerHelper.saveAllItems(valueOutput.child("OutputInventory"), outputInventory.getItems());
        
        if (trSlotConfig != null) {
            valueOutput.store("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC, trSlotConfig.serialize());
        }
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        Optional<ItemStack> optionalVillager1 = valueInput.read("Villager1", ItemStack.CODEC);
        if (optionalVillager1.isPresent()) {
            villager1 = optionalVillager1.get();
            villagerEntity1 = null;
        } else {
            removeVillager1();
        }
        Optional<ItemStack> optionalVillager2 = valueInput.read("Villager2", ItemStack.CODEC);
        if (optionalVillager2.isPresent()) {
            villager2 = optionalVillager2.get();
            villagerEntity2 = null;
        } else {
            removeVillager2();
        }


        valueInput.child("FoodInventory").ifPresent(input -> ContainerHelper.loadAllItems(input, foodInventory.getItems()));
        valueInput.child("OutputInventory").ifPresent(input -> ContainerHelper.loadAllItems(input, outputInventory.getItems()));

        if (trSlotConfig != null && valueInput.contains("TRSlotConfig")) {
            valueInput.read("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC).ifPresent(tag -> trSlotConfig.deserialize(tag));
        }

        super.loadAdditional(valueInput);
    }

    public Container getFoodInventory() {
        return foodInventory;
    }

    public Container getOutputInventory() {
        return outputInventory;
    }

}
