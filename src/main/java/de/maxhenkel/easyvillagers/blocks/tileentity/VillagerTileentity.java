package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class VillagerTileentity extends FakeWorldTileentity {

    private ItemStack villager;
    private EasyVillagerEntity villagerEntity;

    public VillagerTileentity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        villager = ItemStack.EMPTY;
    }

    public ItemStack getVillager() {
        if (villagerEntity != null) {
            ModItems.VILLAGER.setVillager(villager, villagerEntity);
        }
        return villager;
    }

    public boolean hasVillager() {
        return !villager.isEmpty();
    }

    public EasyVillagerEntity getVillagerEntity() {
        if (villagerEntity == null && !villager.isEmpty()) {
            villagerEntity = ModItems.VILLAGER.getVillager(world, villager);
        }
        return villagerEntity;
    }

    public void setVillager(ItemStack villager) {
        this.villager = villager;

        if (villager.isEmpty()) {
            villagerEntity = null;
        } else {
            villagerEntity = ModItems.VILLAGER.getVillager(world, villager);
            onAddVillager(villagerEntity);
        }
        markDirty();
        sync();
    }

    protected void onAddVillager(EasyVillagerEntity villager) {

    }

    public ItemStack removeVillager() {
        ItemStack v = getVillager();
        setVillager(ItemStack.EMPTY);
        return v;
    }

    public boolean advanceAge() {
        return advanceAge(getVillagerEntity());
    }

    public boolean advanceAge(int amount) {
        return advanceAge(getVillagerEntity(), amount);
    }

    public static boolean advanceAge(EasyVillagerEntity villagerEntity, int amount) {
        if (villagerEntity == null) {
            return false;
        }
        int prevAge = villagerEntity.getGrowingAge();
        int age = prevAge + amount;
        villagerEntity.setGrowingAge(age);
        return prevAge < 0 && age >= 0;
    }

    public static boolean advanceAge(EasyVillagerEntity villagerEntity) {
        return advanceAge(villagerEntity, 1);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (hasVillager()) {
            CompoundNBT comp = new CompoundNBT();
            getVillager().write(comp);
            compound.put("Villager", comp);
        }
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        if (compound.contains("Villager")) {
            CompoundNBT comp = compound.getCompound("Villager");
            villager = ItemStack.read(comp);
            villagerEntity = null;
        } else {
            removeVillager();
        }
        super.read(state, compound);
    }

}
