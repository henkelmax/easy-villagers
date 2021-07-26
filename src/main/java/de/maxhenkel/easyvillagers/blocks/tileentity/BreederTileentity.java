package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.entity.EntityUtils;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.corelib.net.NetUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessageVillagerParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class BreederTileentity extends FakeWorldTileentity implements IServerTickableBlockEntity {

    private NonNullList<ItemStack> foodInventory;
    private NonNullList<ItemStack> outputInventory;
    private ItemStack villager1;
    private EasyVillagerEntity villagerEntity1;
    private ItemStack villager2;
    private EasyVillagerEntity villagerEntity2;

    public BreederTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.BREEDER, ModBlocks.BREEDER.defaultBlockState(), pos, state);
        foodInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        villager1 = ItemStack.EMPTY;
        villager2 = ItemStack.EMPTY;
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
            villagerEntity1 = ModItems.VILLAGER.getVillager(level, villager1);
        }
        return villagerEntity1;
    }

    public EasyVillagerEntity getVillagerEntity2() {
        if (villagerEntity2 == null && !villager2.isEmpty()) {
            villagerEntity2 = ModItems.VILLAGER.getVillager(level, villager2);
        }
        return villagerEntity2;
    }

    public void setVillager1(ItemStack villager) {
        this.villager1 = villager;

        if (villager.isEmpty()) {
            villagerEntity1 = null;
        } else {
            villagerEntity1 = ModItems.VILLAGER.getVillager(level, villager);
        }
        setChanged();
        sync();
    }

    public void setVillager2(ItemStack villager) {
        this.villager2 = villager;

        if (villager.isEmpty()) {
            villagerEntity2 = null;
        } else {
            villagerEntity2 = ModItems.VILLAGER.getVillager(level, villager);
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
        if (level.isClientSide) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ParticleTypes.HEART,
                        worldPosition.getX() + (level.random.nextDouble() - 0.5D) + 0.5D,
                        worldPosition.getY() + level.random.nextDouble() + 1D,
                        worldPosition.getZ() + (level.random.nextDouble() - 0.5D) + 0.5D,
                        0D, 0D, 0D);
            }
        } else {
            MessageVillagerParticles msg = new MessageVillagerParticles(worldPosition);
            EntityUtils.forEachPlayerAround((ServerLevel) level, worldPosition, 128, playerEntity -> NetUtils.sendTo(Main.SIMPLE_CHANNEL, playerEntity, msg));
        }
    }

    private boolean addVillager() {
        for (int i = 0; i < outputInventory.size(); i++) {
            if (outputInventory.get(i).isEmpty()) {
                EasyVillagerEntity villagerEntity = new EasyVillagerEntity(EntityType.VILLAGER, level);
                villagerEntity.setVillagerData(villagerEntity.getVillagerData().setType(VillagerType.byBiome(level.getBiomeName(getBlockPos()))));
                villagerEntity.setAge(-24000);
                ItemStack villager = new ItemStack(ModItems.VILLAGER);
                ModItems.VILLAGER.setVillager(villager, villagerEntity);
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
    public CompoundTag save(CompoundTag compound) {
        if (hasVillager1()) {
            CompoundTag comp = new CompoundTag();
            if (villagerEntity1 != null) {
                ModItems.VILLAGER.setVillager(villager1, villagerEntity1);
            }
            villager1.save(comp);
            compound.put("Villager1", comp);
        }
        if (hasVillager2()) {
            CompoundTag comp = new CompoundTag();
            if (villagerEntity2 != null) {
                ModItems.VILLAGER.setVillager(villager2, villagerEntity2);
            }
            villager2.save(comp);
            compound.put("Villager2", comp);
        }
        compound.put("FoodInventory", ContainerHelper.saveAllItems(new CompoundTag(), foodInventory, true));
        compound.put("OutputInventory", ContainerHelper.saveAllItems(new CompoundTag(), outputInventory, true));
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("Villager1")) {
            CompoundTag comp = compound.getCompound("Villager1");
            villager1 = ItemStack.of(comp);
            villagerEntity1 = null;
        } else {
            removeVillager1();
        }
        if (compound.contains("Villager2")) {
            CompoundTag comp = compound.getCompound("Villager2");
            villager2 = ItemStack.of(comp);
            villagerEntity2 = null;
        } else {
            removeVillager2();
        }
        ContainerHelper.loadAllItems(compound.getCompound("FoodInventory"), foodInventory);
        ContainerHelper.loadAllItems(compound.getCompound("OutputInventory"), outputInventory);
        super.load(compound);
    }

    public Container getFoodInventory() {
        return new ItemListInventory(foodInventory, this::setChanged);
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
                return LazyOptional.of(this::getFoodInventoryItemHandler).cast();
            }

        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable foodInventoryHandler;

    public IItemHandlerModifiable getFoodInventoryItemHandler() {
        if (foodInventoryHandler == null) {
            foodInventoryHandler = new ItemStackHandler(foodInventory);
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
