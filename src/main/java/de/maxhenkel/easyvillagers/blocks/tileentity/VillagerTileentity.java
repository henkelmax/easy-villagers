package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VillagerTileentity extends FakeWorldTileentity {

    protected ItemStack villager;
    protected EasyVillagerEntity villagerEntity;

    public VillagerTileentity(BlockEntityType<?> type, BlockState defaultState, BlockPos pos, BlockState state) {
        super(type, defaultState, pos, state);
        villager = ItemStack.EMPTY;
    }

    public ItemStack getVillager() {
        if (villagerEntity != null) {
            ModItems.VILLAGER.get().setVillager(villager, villagerEntity);
        }
        return villager;
    }

    public boolean hasVillager() {
        return !villager.isEmpty();
    }

    public EasyVillagerEntity getVillagerEntity() {
        if (villagerEntity == null && !villager.isEmpty()) {
            villagerEntity = ModItems.VILLAGER.get().getVillager(level, villager);
        }
        return villagerEntity;
    }

    public void setVillager(ItemStack villager) {
        this.villager = villager;

        removeTradingPlayer();

        if (villager.isEmpty()) {
            villagerEntity = null;
        } else {
            villagerEntity = ModItems.VILLAGER.get().getVillager(level, villager);
            onAddVillager(villagerEntity);
        }
        setChanged();
        sync();
    }

    public void removeTradingPlayer() {
        if (villagerEntity != null) {
            villagerEntity.setTradingPlayer(null);
        }
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
        int prevAge = villagerEntity.getAge();
        int age = prevAge + amount;
        villagerEntity.setAge(age);
        return prevAge < 0 && age >= 0;
    }

    public static boolean advanceAge(EasyVillagerEntity villagerEntity) {
        return advanceAge(villagerEntity, 1);
    }

    @Override
    public void setRemoved() {
        removeTradingPlayer();
        super.setRemoved();
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        if (hasVillager()) {
            CompoundTag comp = new CompoundTag();
            getVillager().save(comp);
            compound.put("Villager", comp);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("Villager")) {
            CompoundTag comp = compound.getCompound("Villager");
            villager = ItemStack.of(comp);
            villagerEntity = null;
        } else {
            removeVillager();
        }
        super.load(compound);
    }

}
