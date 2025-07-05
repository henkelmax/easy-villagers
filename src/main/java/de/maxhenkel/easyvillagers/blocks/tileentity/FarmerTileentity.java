package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.codec.ValueInputOutputUtils;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.OutputItemHandler;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class FarmerTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    protected BlockState crop;
    protected NonNullList<ItemStack> inventory;
    protected ItemStackHandler itemHandler;
    protected OutputItemHandler outputItemHandler;

    public FarmerTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.FARMER.get(), ModBlocks.FARMER.get().defaultBlockState(), pos, state);
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        itemHandler = new ItemStackHandler(inventory);
        outputItemHandler = new OutputItemHandler(inventory);
    }

    @Override
    protected void onAddVillager(EasyVillagerEntity villager) {
        super.onAddVillager(villager);
        if (villager.getVillagerXp() <= 0 && !villager.getVillagerData().profession().is(VillagerProfession.NITWIT)) {
            villager.setVillagerData(villager.getVillagerData().withProfession(level.registryAccess(), VillagerProfession.FARMER));
        }
    }

    public void setCrop(Item seed) {
        if (seed == null) {
            this.crop = null;
        } else {
            this.crop = getSeedCrop(seed);
        }
        setChanged();
        sync();
    }

    public Block removeSeed() {
        if (crop == null) {
            return null;
        }
        Block s = crop.getBlock();
        setCrop(null);
        return s;
    }

    public boolean isValidSeed(Item seed) {
        return getSeedCrop(seed) != null;
    }

    public BlockState getSeedCrop(Item seed) {
        ItemStack seedStack = new ItemStack(seed);
        if (!(seed instanceof BlockItem blockitem)) {
            return null;
        }
        if (!seedStack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
            return null;
        }
        if (EasyVillagersMod.SERVER_CONFIG.farmCropsBlacklist.stream().anyMatch(itemTag -> itemTag.contains(seed))) {
            return null;
        }
        return blockitem.getBlock().defaultBlockState();
    }

    @Nullable
    public BlockState getCrop() {
        return crop;
    }

    @Override
    public void tickServer() {
        EasyVillagerEntity v = getVillagerEntity();
        if (v != null) {
            VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);

            if (advanceAge()) {
                sync();
            }
            setChanged();
        }

        if (level.getGameTime() % 20 == 0 && level.random.nextInt(EasyVillagersMod.SERVER_CONFIG.farmSpeed.get()) == 0) {
            if (ageCrop(v)) {
                sync();
                setChanged();
            }
        }
    }

    private boolean ageCrop(@Nullable EasyVillagerEntity villager) {
        BlockState c = getCrop();
        if (c == null) {
            return false;
        }

        Optional<Property<?>> ageProp = c.getProperties().stream().filter(p -> p.getName().equals("age")).findFirst();

        if (!ageProp.isPresent() || !(ageProp.get() instanceof IntegerProperty)) {
            return false;
        }

        IntegerProperty p = (IntegerProperty) ageProp.get();
        Integer max = p.getPossibleValues().stream().max(Integer::compare).get();

        int age = c.getValue(p);

        if (age >= max) {
            if (villager == null || villager.isBaby() || !villager.getVillagerData().profession().is(VillagerProfession.FARMER)) {
                return false;
            }
            LootParams.Builder context = new LootParams.Builder((ServerLevel) level).withParameter(LootContextParams.ORIGIN, new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ())).withParameter(LootContextParams.BLOCK_STATE, c).withParameter(LootContextParams.TOOL, ItemStack.EMPTY);
            List<ItemStack> drops = c.getDrops(context);
            for (ItemStack stack : drops) {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    stack = itemHandler.insertItem(i, stack, false);
                }
            }

            crop = crop.setValue(p, 0);
            VillagerBlockBase.playVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_WORK_FARMER);
            return true;
        } else {
            crop = crop.setValue(p, age + 1);
            return true;
        }
    }

    public Container getOutputInventory() {
        return new ItemListInventory(inventory, this::setChanged);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        if (crop != null) {
            ValueInputOutputUtils.setTag(valueOutput, "Crop", NbtUtils.writeBlockState(crop));
        }
        ContainerHelper.saveAllItems(valueOutput, inventory, false);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        Optional<BlockState> optionalCrop = ValueInputOutputUtils.getTag(valueInput, "Crop").map(t -> NbtUtils.readBlockState(valueInput.lookup().lookupOrThrow(Registries.BLOCK), t));
        if (optionalCrop.isPresent()) {
            crop = optionalCrop.get();
        } else {
            removeSeed();
        }

        ContainerHelper.loadAllItems(valueInput, inventory);
        super.loadAdditional(valueInput);
    }

    public IItemHandler getItemHandler() {
        return outputItemHandler;
    }

}
