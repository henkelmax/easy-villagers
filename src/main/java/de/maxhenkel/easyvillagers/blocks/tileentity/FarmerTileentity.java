package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.blocks.tileentity.IServerTickableBlockEntity;
// import de.maxhenkel.corelib.codec.ValueInputOutputUtils;
import de.maxhenkel.easyvillagers.inventory.SimpleInventory;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible;
import de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;

public class FarmerTileentity extends VillagerTileentity implements IServerTickableBlockEntity, ITRCompatible {

    protected TRSlotConfiguration trSlotConfig;

    @Override
    public Object getSlotConfiguration() {
        return trSlotConfig;
    }


    protected BlockState crop;
    protected SimpleInventory inventory;

    @SuppressWarnings("this-escape")
    public FarmerTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.FARMER, ModBlocks.FARMER.defaultBlockState(), pos, state);
        inventory = new SimpleInventory(4, this::setChanged);
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
            trSlotConfig = new TRSlotConfiguration();
        }
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
        if (EasyVillagersMod.CONFIG.server.farmCropsBlacklist.stream().anyMatch(itemTag -> itemTag.equals(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(seed).toString()))) {
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

        if (level.getGameTime() % 20 == 0 && level.getRandom().nextInt(EasyVillagersMod.CONFIG.server.farmSpeed.get()) == 0) {
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
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    if (stack.isEmpty()) {
                        break;
                    }
                    ItemStack invStack = inventory.getItem(i);
                    if (invStack.isEmpty()) {
                        inventory.setItem(i, stack.copy());
                        stack.setCount(0);
                    } else if (ItemStack.isSameItemSameComponents(invStack, stack) && invStack.getCount() < invStack.getMaxStackSize()) {
                        int amount = Math.min(stack.getCount(), invStack.getMaxStackSize() - invStack.getCount());
                        invStack.grow(amount);
                        stack.shrink(amount);
                    }
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
        return inventory;
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        if (crop != null) {
            valueOutput.storeNullable("Crop", net.minecraft.world.level.block.state.BlockState.CODEC, crop);
    
        if (trSlotConfig != null) {
            valueOutput.store("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC, trSlotConfig.serialize());
        }
    }
        ContainerHelper.saveAllItems(valueOutput, inventory.getItems());
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        Optional<BlockState> optionalCrop = valueInput.read("Crop", net.minecraft.world.level.block.state.BlockState.CODEC);
        if (optionalCrop.isPresent()) {
            crop = optionalCrop.get();
        } else {
            removeSeed();
        }

        ContainerHelper.loadAllItems(valueInput, inventory.getItems());

        if (trSlotConfig != null && valueInput.contains("TRSlotConfig")) {
            valueInput.read("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC).ifPresent(tag -> trSlotConfig.deserialize(tag));
        }

        super.loadAdditional(valueInput);
    }

}

