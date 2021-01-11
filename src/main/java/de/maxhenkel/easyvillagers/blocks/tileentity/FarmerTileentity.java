package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.inventory.ItemListInventory;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
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

public class FarmerTileentity extends VillagerTileentity implements ITickableTileEntity {

    private BlockState crop;
    private NonNullList<ItemStack> inventory;

    public FarmerTileentity() {
        super(ModTileEntities.FARMER, ModBlocks.FARMER.getDefaultState());
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    protected void onAddVillager(EasyVillagerEntity villager) {
        super.onAddVillager(villager);
        if (villager.getXp() <= 0) {
            villager.setVillagerData(villager.getVillagerData().withProfession(VillagerProfession.FARMER));
        }
    }

    public void setCrop(Item seed) {
        if (seed == null) {
            this.crop = null;
        } else {
            this.crop = getSeedCrop(seed);
        }
        markDirty();
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
            return Blocks.WHEAT.getDefaultState();
        } else if (seed == Items.POTATO) {
            return Blocks.POTATOES.getDefaultState();
        } else if (seed == Items.CARROT) {
            return Blocks.CARROTS.getDefaultState();
        } else if (seed == Items.BEETROOT_SEEDS) {
            return Blocks.BEETROOTS.getDefaultState();
        } else if (seed instanceof IPlantable) {
            IPlantable plantable = (IPlantable) seed;
            if (plantable.getPlantType(world, getPos()) == PlantType.CROP) { //TODO fake world
                return plantable.getPlant(world, getPos());
            }
        }
        return null;
    }

    @Nullable
    public BlockState getCrop() {
        return crop;
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            return;
        }
        EasyVillagerEntity v = getVillagerEntity();
        if (v != null) {
            VillagerBlockBase.playRandomVillagerSound(world, getPos(), SoundEvents.ENTITY_VILLAGER_AMBIENT);

            if (advanceAge()) {
                sync();
            }
            markDirty();
        }

        if (world.getGameTime() % 20 == 0 && world.rand.nextInt(Main.SERVER_CONFIG.farmSpeed.get()) == 0) {
            if (ageCrop(v)) {
                sync();
                markDirty();
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
        Integer max = p.getAllowedValues().stream().max(Integer::compare).get();

        int age = c.get(p);

        if (age >= max) {
            if (villager == null || villager.isChild() || !villager.getVillagerData().getProfession().equals(VillagerProfession.FARMER)) {
                return false;
            }
            LootContext.Builder context = new LootContext.Builder((ServerWorld) world).withParameter(LootParameters.field_237457_g_, new Vector3d(pos.getX(), pos.getY(), pos.getZ())).withParameter(LootParameters.BLOCK_STATE, c).withParameter(LootParameters.TOOL, ItemStack.EMPTY);
            List<ItemStack> drops = c.getDrops(context);
            IItemHandlerModifiable itemHandler = getItemHandler();
            for (ItemStack stack : drops) {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    stack = itemHandler.insertItem(i, stack, false);
                }
            }

            crop = crop.with(p, 0);
            VillagerBlockBase.playVillagerSound(world, getPos(), SoundEvents.ENTITY_VILLAGER_WORK_FARMER);
            return true;
        } else {
            crop = crop.with(p, age + 1);
            return true;
        }
    }

    public IInventory getOutputInventory() {
        return new ItemListInventory(inventory, this::markDirty);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (crop != null) {
            compound.put("Crop", NBTUtil.writeBlockState(crop));
        }
        ItemStackHelper.saveAllItems(compound, inventory, false);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        if (compound.contains("Crop")) {
            crop = NBTUtil.readBlockState(compound.getCompound("Crop"));
        } else {
            removeSeed();
        }

        ItemStackHelper.loadAllItems(compound, inventory);
        super.read(state, compound);
    }

    private IItemHandlerModifiable handler;

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
