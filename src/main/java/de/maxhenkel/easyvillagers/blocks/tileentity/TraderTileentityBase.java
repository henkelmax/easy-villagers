package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TraderTileentityBase extends VillagerTileentity implements IServerTickableBlockEntity {

    protected Block workstation;
    protected long nextRestock;

    public TraderTileentityBase(BlockEntityType<?> type, BlockState defaultState, BlockPos pos, BlockState state) {
        super(type, defaultState, pos, state);
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

        setChanged();
        sync();
    }

    public Block removeWorkstation() {
        Block w = workstation;
        setWorkstation(Blocks.AIR);
        return w;
    }

    public boolean isValidBlock(Block block) {
        return PoiTypes.forState(block.defaultBlockState()).isPresent();
    }

    public VillagerProfession getWorkstationProfession() {
        return PoiTypes.forState(workstation.defaultBlockState()).flatMap(pointOfInterestType -> BuiltInRegistries.VILLAGER_PROFESSION.stream().filter(villagerProfession -> villagerProfession.heldJobSite().test(pointOfInterestType)).findFirst()).orElse(VillagerProfession.NONE);
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
        if (v == null || v.getVillagerXp() > 0 || v.getVillagerData().getProfession().equals(VillagerProfession.NITWIT)) {
            return;
        }
        v.setVillagerData(v.getVillagerData().setProfession(getWorkstationProfession()));
    }

    public boolean openTradingGUI(Player playerEntity) {
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return false;
        }

        if (villagerEntity.isBaby()) {
            return false;
        }

        VillagerProfession profession = villagerEntity.getVillagerData().getProfession();
        if (profession.equals(VillagerProfession.NONE) || profession.equals(VillagerProfession.NITWIT)) {
            return false;
        }

        if (villagerEntity.isTrading()) {
            return false;
        }

        villagerEntity.setPos(getBlockPos().getX() + 0.5D, getBlockPos().getY() + 1D, getBlockPos().getZ() + 0.5D);
        villagerEntity.startTrading(playerEntity);
        return true;
    }

    @Override
    public void tickServer() {
        EasyVillagerEntity v = getVillagerEntity();
        if (v == null) {
            return;
        }

        if (advanceAge()) {
            sync();
        }
        setChanged();

        VillagerBlockBase.playRandomVillagerSound(level, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);

        if (!v.isTrading()) {
            if (v.increaseProfessionLevelOnUpdate) {
                v.increaseMerchantCareer();
                v.increaseProfessionLevelOnUpdate = false;
                sync();
            }

            if (level.getGameTime() - getLastRestock() > nextRestock && v.getVillagerData().getProfession().equals(getWorkstationProfession())) {
                restock();
                nextRestock = calculateNextRestock();
            }
        }
    }

    protected long calculateNextRestock() {
        return Main.SERVER_CONFIG.traderMinRestockTime.get() + level.random.nextInt(Math.max(Main.SERVER_CONFIG.traderMaxRestockTime.get() - Main.SERVER_CONFIG.traderMinRestockTime.get(), 1));
    }

    protected void restock() {
        try {
            EasyVillagerEntity villagerEntity = getVillagerEntity();
            if (villagerEntity == null) {
                return;
            }
            villagerEntity.restock();
            SoundEvent workSound = villagerEntity.getVillagerData().getProfession().workSound();
            if (workSound != null) {
                VillagerBlockBase.playVillagerSound(level, getBlockPos(), workSound);
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error restocking villager", e);
        }
    }

    protected long getLastRestock() {
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return 0L;
        }
        return villagerEntity.lastRestockGameTime;
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);

        if (hasWorkstation()) {
            compound.putString("Workstation", BuiltInRegistries.BLOCK.getKey(workstation).toString());
        }
        compound.putLong("NextRestock", nextRestock);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        if (compound.contains("Workstation")) {
            workstation = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(compound.getString("Workstation")));
        } else {
            removeWorkstation();
        }
        nextRestock = compound.getLong("NextRestock");
        super.loadAdditional(compound, provider);
    }

}
