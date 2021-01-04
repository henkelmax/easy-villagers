package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.entity.EntityUtils;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.corelib.net.NetUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessageVillagerParticles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
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

public class BreederTileentity extends FakeWorldTileentity implements ITickableTileEntity {

    private NonNullList<ItemStack> foodInventory;
    private NonNullList<ItemStack> outputInventory;
    private ItemStack villager1;
    private EasyVillagerEntity villagerEntity1;
    private ItemStack villager2;
    private EasyVillagerEntity villagerEntity2;

    public BreederTileentity() {
        super(ModTileEntities.BREEDER);
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
            villagerEntity1 = ModItems.VILLAGER.getVillager(world, villager1);
        }
        return villagerEntity1;
    }

    public EasyVillagerEntity getVillagerEntity2() {
        if (villagerEntity2 == null && !villager2.isEmpty()) {
            villagerEntity2 = ModItems.VILLAGER.getVillager(world, villager2);
        }
        return villagerEntity2;
    }

    public void setVillager1(ItemStack villager) {
        this.villager1 = villager;

        if (villager.isEmpty()) {
            villagerEntity1 = null;
        } else {
            villagerEntity1 = ModItems.VILLAGER.getVillager(world, villager);
        }
        markDirty();
        sync();
    }

    public void setVillager2(ItemStack villager) {
        this.villager2 = villager;

        if (villager.isEmpty()) {
            villagerEntity2 = null;
        } else {
            villagerEntity2 = ModItems.VILLAGER.getVillager(world, villager);
        }
        markDirty();
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
    public void tick() {
        if (world.isRemote) {
            return;
        }

        boolean age1 = VillagerTileentity.advanceAge(getVillagerEntity1());
        boolean age2 = VillagerTileentity.advanceAge(getVillagerEntity2());
        if (age1 || age2) {
            sync();
        }
        if (hasVillager1() || hasVillager2()) {
            markDirty();
        }

        if (world.getGameTime() % Main.SERVER_CONFIG.breedingTime.get() == 0) {
            tryBreed();
        }
    }

    public void tryBreed() {
        if (canBreed() && addVillager()) {
            removeBreedingItems();
            TraderBlock.playVillagerSound(world, pos, SoundEvents.ENTITY_VILLAGER_CELEBRATE);
            spawnParticles();
        }
    }

    public void spawnParticles() {
        if (world.isRemote) {
            for (int i = 0; i < 5; i++) {
                world.addParticle(ParticleTypes.HEART,
                        pos.getX() + (world.rand.nextDouble() - 0.5D) + 0.5D,
                        pos.getY() + world.rand.nextDouble() + 1D,
                        pos.getZ() + (world.rand.nextDouble() - 0.5D) + 0.5D,
                        0D, 0D, 0D);
            }
        } else {
            MessageVillagerParticles msg = new MessageVillagerParticles(pos);
            EntityUtils.forEachPlayerAround((ServerWorld) world, pos, 128, playerEntity -> NetUtils.sendTo(Main.SIMPLE_CHANNEL, playerEntity, msg));
        }
    }

    private boolean addVillager() {
        for (int i = 0; i < outputInventory.size(); i++) {
            if (outputInventory.get(i).isEmpty()) {
                EasyVillagerEntity villagerEntity = new EasyVillagerEntity(EntityType.VILLAGER, world);
                villagerEntity.setVillagerData(villagerEntity.getVillagerData().withType(VillagerType.func_242371_a(world.func_242406_i(getPos()))));
                villagerEntity.setGrowingAge(-24000);
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
        if (getVillagerEntity1().isChild() || getVillagerEntity2().isChild()) {
            return false;
        }
        int value = 0;
        for (ItemStack stack : foodInventory) {
            value += VillagerEntity.FOOD_VALUES.getOrDefault(stack.getItem(), 0) * stack.getCount();
        }
        return value >= 24;
    }

    private void removeBreedingItems() {
        int value = 0;
        for (ItemStack stack : foodInventory) {
            for (int i = 0; i < stack.getCount(); i++) {
                value += VillagerEntity.FOOD_VALUES.getOrDefault(stack.getItem(), 0);
                stack.shrink(1);
                if (value >= 24) {
                    return;
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (hasVillager1()) {
            CompoundNBT comp = new CompoundNBT();
            if (villagerEntity1 != null) {
                ModItems.VILLAGER.setVillager(villager1, villagerEntity1);
            }
            villager1.write(comp);
            compound.put("Villager1", comp);
        }
        if (hasVillager2()) {
            CompoundNBT comp = new CompoundNBT();
            if (villagerEntity2 != null) {
                ModItems.VILLAGER.setVillager(villager2, villagerEntity2);
            }
            villager2.write(comp);
            compound.put("Villager2", comp);
        }
        compound.put("FoodInventory", ItemStackHelper.saveAllItems(new CompoundNBT(), foodInventory, true));
        compound.put("OutputInventory", ItemStackHelper.saveAllItems(new CompoundNBT(), outputInventory, true));
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        if (compound.contains("Villager1")) {
            CompoundNBT comp = compound.getCompound("Villager1");
            villager1 = ItemStack.read(comp);
            villagerEntity1 = null;
        } else {
            removeVillager1();
        }
        if (compound.contains("Villager2")) {
            CompoundNBT comp = compound.getCompound("Villager2");
            villager2 = ItemStack.read(comp);
            villagerEntity2 = null;
        } else {
            removeVillager2();
        }
        ItemStackHelper.loadAllItems(compound.getCompound("FoodInventory"), foodInventory);
        ItemStackHelper.loadAllItems(compound.getCompound("OutputInventory"), outputInventory);
        super.read(state, compound);
    }

    public IInventory getFoodInventory() {
        return new ItemListInventory(foodInventory, this::markDirty);
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
