package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class VillagerTileentity extends FakeWorldTileentity {

    private ItemStack villager;
    private VillagerEntity villagerEntity;

    public VillagerTileentity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        villager = ItemStack.EMPTY;
    }

    public ItemStack getVillager() {
        return villager;
    }

    public boolean hasVillager() {
        return !villager.isEmpty();
    }

    public VillagerEntity getVillagerEntity() {
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

    protected void onAddVillager(VillagerEntity villager) {

    }

    public ItemStack removeVillager() {
        ItemStack v = villager;
        setVillager(ItemStack.EMPTY);
        return v;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (hasVillager()) {
            CompoundNBT comp = new CompoundNBT();
            if (villagerEntity != null) {
                ModItems.VILLAGER.setVillager(villager, villagerEntity);
            }
            villager.write(comp);
            compound.put("Villager", comp);
        }
        return super.write(compound);
    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        if (compound.contains("Villager")) {
            CompoundNBT comp = compound.getCompound("Villager");
            villager = ItemStack.read(comp);
            villagerEntity = null;
        }
        super.func_230337_a_(state, compound);
    }

}
