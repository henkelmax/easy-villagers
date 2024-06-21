package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.MultiItemStackHandler;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.FoodSlot;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessageVillagerParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class BreederTileentity extends FakeWorldTileentity implements IServerTickableBlockEntity {

    protected NonNullList<ItemStack> foodInventory;
    protected NonNullList<ItemStack> outputInventory;
    protected ItemStack villager1;
    protected EasyVillagerEntity villagerEntity1;
    protected ItemStack villager2;
    protected EasyVillagerEntity villagerEntity2;
    private MultiItemStackHandler itemHandler;

    public BreederTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.BREEDER.get(), ModBlocks.BREEDER.get().defaultBlockState(), pos, state);
        foodInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        villager1 = ItemStack.EMPTY;
        villager2 = ItemStack.EMPTY;
        itemHandler = new MultiItemStackHandler(foodInventory, outputInventory, FoodSlot::isValid);
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
        if (level.isClientSide) {
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

        if (level.getGameTime() % Main.SERVER_CONFIG.breedingTime.get() == 0) {
            tryBreed();
        }
    }

    public void tryBreed() {
        if (canBreed() && addVillager()) {
            removeBreedingItems();
            VillagerBlockBase.playVillagerSound(level, worldPosition, SoundEvents.VILLAGER_CELEBRATE);
            spawnParticles();
        }
    }

    public void spawnParticles() {
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(worldPosition), new MessageVillagerParticles(worldPosition));

        } else if (level.isClientSide) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ParticleTypes.HEART,
                        worldPosition.getX() + (level.random.nextDouble() - 0.5D) + 0.5D,
                        worldPosition.getY() + level.random.nextDouble() + 1D,
                        worldPosition.getZ() + (level.random.nextDouble() - 0.5D) + 0.5D,
                        0D, 0D, 0D);
            }
        }
    }

    private boolean addVillager() {
        for (int i = 0; i < outputInventory.size(); i++) {
            if (outputInventory.get(i).isEmpty()) {
                EasyVillagerEntity villagerEntity = new EasyVillagerEntity(EntityType.VILLAGER, level);
                villagerEntity.setVillagerData(villagerEntity.getVillagerData().setType(VillagerType.byBiome(level.getBiome(getBlockPos()))));
                villagerEntity.setAge(-24000);
                ItemStack villager = new ItemStack(ModItems.VILLAGER.get());
                VillagerData.applyToItem(villager, villagerEntity);
                outputInventory.set(i, villager);
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
        for (ItemStack stack : foodInventory) {
            value += Villager.FOOD_POINTS.getOrDefault(stack.getItem(), 0) * stack.getCount();
        }
        return value >= 24;
    }

    private void removeBreedingItems() {
        int value = 0;
        for (ItemStack stack : foodInventory) {
            for (int i = 0; i < stack.getCount(); i++) {
                value += Villager.FOOD_POINTS.getOrDefault(stack.getItem(), 0);
                stack.shrink(1);
                if (value >= 24) {
                    return;
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);

        if (hasVillager1()) {
            compound.put("Villager1", getVillager1().save(provider));
        }
        if (hasVillager2()) {
            compound.put("Villager2", getVillager2().save(provider));
        }
        compound.put("FoodInventory", ContainerHelper.saveAllItems(new CompoundTag(), foodInventory, true, provider));
        compound.put("OutputInventory", ContainerHelper.saveAllItems(new CompoundTag(), outputInventory, true, provider));
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        if (compound.contains("Villager1")) {
            CompoundTag comp = compound.getCompound("Villager1");
            villager1 = VillagerData.convert(provider, comp);
            villagerEntity1 = null;
        } else {
            removeVillager1();
        }
        if (compound.contains("Villager2")) {
            CompoundTag comp = compound.getCompound("Villager2");
            villager2 = VillagerData.convert(provider, comp);
            villagerEntity2 = null;
        } else {
            removeVillager2();
        }
        VillagerData.convertInventory(compound.getCompound("FoodInventory"), foodInventory, provider);
        VillagerData.convertInventory(compound.getCompound("OutputInventory"), outputInventory, provider);
        super.loadAdditional(compound, provider);
    }

    public Container getFoodInventory() {
        return new ItemListInventory(foodInventory, this::setChanged);
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory, this::setChanged);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

}
