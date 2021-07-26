package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class FarmerTileentity extends VillagerTileentity implements IServerTickableBlockEntity {

    private BlockState crop;
    private NonNullList<ItemStack> inventory;

    public FarmerTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.FARMER, ModBlocks.FARMER.defaultBlockState(), pos, state);
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    protected void onAddVillager(EasyVillagerEntity villager) {
        super.onAddVillager(villager);
        if (villager.getVillagerXp() <= 0 && !villager.getVillagerData().getProfession().equals(VillagerProfession.NITWIT)) {
            villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.FARMER));
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
        if (seed == Items.WHEAT_SEEDS) {
            return Blocks.WHEAT.defaultBlockState();
        } else if (seed == Items.POTATO) {
            return Blocks.POTATOES.defaultBlockState();
        } else if (seed == Items.CARROT) {
            return Blocks.CARROTS.defaultBlockState();
        } else if (seed == Items.BEETROOT_SEEDS) {
            return Blocks.BEETROOTS.defaultBlockState();
        } else if (seed instanceof IPlantable) {
            IPlantable plantable = (IPlantable) seed;
            if (plantable.getPlantType(level, getBlockPos()) == PlantType.CROP) { //TODO fake world
                return plantable.getPlant(level, getBlockPos());
            }
        }
        return null;
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

        if (level.getGameTime() % 20 == 0 && level.random.nextInt(Main.SERVER_CONFIG.farmSpeed.get()) == 0) {
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
            if (villager == null || villager.isBaby() || !villager.getVillagerData().getProfession().equals(VillagerProfession.FARMER)) {
                return false;
            }
            LootContext.Builder context = new LootContext.Builder((ServerLevel) level).withParameter(LootContextParams.ORIGIN, new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ())).withParameter(LootContextParams.BLOCK_STATE, c).withParameter(LootContextParams.TOOL, ItemStack.EMPTY);
            List<ItemStack> drops = c.getDrops(context);
            IItemHandlerModifiable itemHandler = getItemHandler();
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
    public CompoundTag save(CompoundTag compound) {
        if (crop != null) {
            compound.put("Crop", NbtUtils.writeBlockState(crop));
        }
        ContainerHelper.saveAllItems(compound, inventory, false);
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("Crop")) {
            crop = NbtUtils.readBlockState(compound.getCompound("Crop"));
        } else {
            removeSeed();
        }

        ContainerHelper.loadAllItems(compound, inventory);
        super.load(compound);
    }

    private IItemHandlerModifiable handler;

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(this::getItemHandler).cast();
        }
        return super.getCapability(cap, side);
    }

    public IItemHandlerModifiable getItemHandler() {
        if (handler == null) {
            handler = new ItemStackHandler(inventory);
        }
        return handler;
    }

}
