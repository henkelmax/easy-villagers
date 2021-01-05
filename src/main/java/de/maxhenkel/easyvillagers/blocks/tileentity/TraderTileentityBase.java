package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class TraderTileentityBase extends VillagerTileentity implements ITickableTileEntity {

    public static final Field LAST_RESTOCK = ObfuscationReflectionHelper.findField(VillagerEntity.class, "field_213785_bP");
    public static final Method RESTOCK = ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_223718_eH");
    public static final Field LEVELED_UP = ObfuscationReflectionHelper.findField(VillagerEntity.class, "field_213777_bF");
    public static final Method LEVEL_UP = ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_175554_cu");
    public static final Method DISPLAY_MERCHANT_GUI = ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_213740_f", PlayerEntity.class);
    public static final Method CAN_LEVEL_UP = ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_213741_eu");

    private Block workstation;
    private long nextRestock;

    public TraderTileentityBase(TileEntityType type) {
        super(type);
        workstation = Blocks.AIR;
    }

    public Block getWorkstation() {
        return workstation;
    }

    public boolean hasWorkstation() {
        return workstation != Blocks.AIR;
    }

    public void setWorkstation(Block workstation) {
        this.workstation = workstation;

        if (hasVillager()) {
            fixProfession();
        }

        markDirty();
        sync();
    }

    public Block removeWorkstation() {
        Block w = workstation;
        setWorkstation(Blocks.AIR);
        return w;
    }

    public boolean isValidBlock(Block block) {
        return PointOfInterestType.forState(block.getDefaultState()).isPresent();
    }

    public VillagerProfession getWorkstationProfession() {
        return PointOfInterestType.forState(workstation.getDefaultState()).flatMap(pointOfInterestType -> ForgeRegistries.PROFESSIONS.getValues().stream().filter(villagerProfession -> villagerProfession.getPointOfInterest() == pointOfInterestType).findFirst()).orElse(VillagerProfession.NONE);
    }

    @Override
    protected void onAddVillager(EasyVillagerEntity villager) {
        super.onAddVillager(villager);

        if (hasWorkstation()) {
            fixProfession();
        }
    }

    private void fixProfession() {
        EasyVillagerEntity v = getVillagerEntity();
        if (v == null || v.getXp() > 0) {
            return;
        }
        v.setVillagerData(v.getVillagerData().withProfession(getWorkstationProfession()));
    }

    public boolean openTradingGUI(PlayerEntity playerEntity) {
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return false;
        }

        if (villagerEntity.isChild()) {
            return false;
        }

        VillagerProfession profession = villagerEntity.getVillagerData().getProfession();
        if (profession.equals(VillagerProfession.NONE) || profession.equals(VillagerProfession.NITWIT)) {
            return false;
        }

        if (villagerEntity.hasCustomer()) {
            return false;
        }

        villagerEntity.setPosition(getPos().getX() + 0.5D, getPos().getY() + 1D, getPos().getZ() + 0.5D);

        try {
            DISPLAY_MERCHANT_GUI.invoke(villagerEntity, playerEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            return;
        }
        EasyVillagerEntity v = getVillagerEntity();
        if (v == null) {
            return;
        }

        if (advanceAge()) {
            sync();
        }
        markDirty();

        VillagerBlockBase.playRandomVillagerSound(world, getPos(), SoundEvents.ENTITY_VILLAGER_AMBIENT);

        if (!v.hasCustomer()) {
            try {
                if ((Boolean) LEVELED_UP.get(v)) {
                    LEVEL_UP.invoke(v);
                    LEVELED_UP.set(v, false);
                    sync();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (world.getGameTime() - getLastRestock() > nextRestock && v.getVillagerData().getProfession().equals(getWorkstationProfession())) {
                restock();
                nextRestock = calculateNextRestock();
            }
        }
    }

    protected long calculateNextRestock() {
        return Main.SERVER_CONFIG.traderMinRestockTime.get() + world.rand.nextInt(Math.max(Main.SERVER_CONFIG.traderMaxRestockTime.get() - Main.SERVER_CONFIG.traderMinRestockTime.get(), 1));
    }

    protected void restock() {
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return;
        }
        try {
            LAST_RESTOCK.set(villagerEntity, world.getGameTime());
            RESTOCK.invoke(villagerEntity);
            VillagerBlockBase.playVillagerSound(world, getPos(), villagerEntity.getVillagerData().getProfession().getSound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected long getLastRestock() {
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return 0L;
        }
        try {
            return (long) LAST_RESTOCK.get(villagerEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (hasWorkstation()) {
            compound.putString("Workstation", workstation.getRegistryName().toString());
        }
        compound.putLong("NextRestock", nextRestock);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        if (compound.contains("Workstation")) {
            workstation = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("Workstation")));
        } else {
            removeWorkstation();
        }
        nextRestock = compound.getLong("NextRestock");
        super.read(state, compound);
    }

}
