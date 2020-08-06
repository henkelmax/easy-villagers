package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.CachedValue;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TraderTileentity extends VillagerTileentity implements ITickableTileEntity {

    private static final CachedValue<Field> LAST_RESTOCK = new CachedValue<>(() -> ObfuscationReflectionHelper.findField(VillagerEntity.class, "field_213785_bP"));
    private static final CachedValue<Method> RESTOCK = new CachedValue<>(() -> ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_223718_eH"));
    private static final CachedValue<Field> LEVELED_UP = new CachedValue<>(() -> ObfuscationReflectionHelper.findField(VillagerEntity.class, "field_213777_bF"));
    private static final CachedValue<Method> LEVEL_UP = new CachedValue<>(() -> ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_175554_cu"));
    private static final CachedValue<Method> DISPLAY_MERCHANT_GUI = new CachedValue<>(() -> ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_213740_f", PlayerEntity.class));

    private Block workstation;
    private long nextRestock;

    public TraderTileentity() {
        super(ModTileEntities.TRADER);
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
    protected void onAddVillager(VillagerEntity villager) {
        super.onAddVillager(villager);

        if (hasWorkstation()) {
            fixProfession();
        }
    }

    private void fixProfession() {
        VillagerEntity v = getVillagerEntity();
        if (v == null || v.getXp() > 0) {
            return;
        }
        v.setVillagerData(v.getVillagerData().withProfession(getWorkstationProfession()));
    }

    public boolean openTradingGUI(PlayerEntity playerEntity) {
        VillagerEntity villagerEntity = getVillagerEntity();
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

        try {
            DISPLAY_MERCHANT_GUI.get().invoke(villagerEntity, playerEntity);
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
        VillagerEntity v = getVillagerEntity();
        if (v == null) {
            return;
        }

        if (advanceAge()) {
            sync();
        }
        markDirty();

        if (world.getGameTime() % 20 == 0 && world.rand.nextInt(40) == 0) {
            TraderBlock.playVillagerSound(world, getPos(), SoundEvents.ENTITY_VILLAGER_AMBIENT);
        }

        if (!v.hasCustomer()) {
            try {
                if ((Boolean) LEVELED_UP.get().get(v)) {
                    LEVEL_UP.get().invoke(v);
                    LEVELED_UP.get().set(v, false);
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

    private long calculateNextRestock() {
        return Main.SERVER_CONFIG.traderMinRestockTime.get() + world.rand.nextInt(Math.max(Main.SERVER_CONFIG.traderMaxRestockTime.get() - Main.SERVER_CONFIG.traderMinRestockTime.get(), 1));
    }

    private void restock() {
        VillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return;
        }
        try {
            LAST_RESTOCK.get().set(villagerEntity, world.getGameTime());
            RESTOCK.get().invoke(villagerEntity);
            TraderBlock.playVillagerSound(world, getPos(), villagerEntity.getVillagerData().getProfession().getSound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getLastRestock() {
        VillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return 0L;
        }
        try {
            return (long) LAST_RESTOCK.get().get(villagerEntity);
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
    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        if (compound.contains("Workstation")) {
            workstation = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("Workstation")));
        } else {
            removeWorkstation();
        }
        nextRestock = compound.getLong("NextRestock");
        super.func_230337_a_(state, compound);
    }

}
