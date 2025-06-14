package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.codec.ValueInputOutputUtils;
import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.Optional;

public class VillagerTileentity extends FakeWorldTileentity {

    protected ItemStack villager;
    protected EasyVillagerEntity villagerEntity;

    public VillagerTileentity(BlockEntityType<?> type, BlockState defaultState, BlockPos pos, BlockState state) {
        super(type, defaultState, pos, state);
        villager = ItemStack.EMPTY;
    }

    public ItemStack getVillager() {
        if (villagerEntity != null) {
            saveVillagerEntity();
        }
        return villager;
    }

    public boolean hasVillager() {
        return !villager.isEmpty();
    }

    @Nullable
    public EasyVillagerEntity getVillagerEntity() {
        if (villagerEntity == null && !villager.isEmpty()) {
            villagerEntity = VillagerData.createEasyVillager(villager, level);
        }
        return villagerEntity;
    }

    public void saveVillagerEntity() {
        if (villagerEntity != null) {
            VillagerData.applyToItem(villager, villagerEntity);
        }
    }

    public void setVillager(ItemStack villager) {
        this.villager = villager;

        removeTradingPlayer();

        if (villager.isEmpty()) {
            villagerEntity = null;
        } else {
            villagerEntity = VillagerData.createEasyVillager(villager, level);
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
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        if (hasVillager()) {
            valueOutput.store("Villager", ItemStack.CODEC, getVillager());
        }
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        Optional<ItemStack> optionalItemStack = ValueInputOutputUtils.getTag(valueInput, "Villager").map(VillagerData::convert);
        if (optionalItemStack.isPresent()) {
            villager = optionalItemStack.get();
            villagerEntity = null;
        } else {
            removeVillager();
        }
        super.loadAdditional(valueInput);
    }

}
