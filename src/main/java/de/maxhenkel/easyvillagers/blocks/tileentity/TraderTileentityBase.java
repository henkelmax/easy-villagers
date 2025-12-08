package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.blockentity.IServerTickableBlockEntity;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;

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

    public Holder<VillagerProfession> getWorkstationProfession() {
        Optional<Holder<PoiType>> poiTypeHolder = PoiTypes.forState(workstation.defaultBlockState());
        if (poiTypeHolder.isEmpty()) {
            return BuiltInRegistries.VILLAGER_PROFESSION.get(VillagerProfession.NONE).orElseThrow();
        }

        Holder<PoiType> poiType = poiTypeHolder.get();
        for (VillagerProfession profession : BuiltInRegistries.VILLAGER_PROFESSION) {
            if (profession.heldJobSite().test(poiType)) {
                return BuiltInRegistries.VILLAGER_PROFESSION.wrapAsHolder(profession);
            }
        }

        return BuiltInRegistries.VILLAGER_PROFESSION.get(VillagerProfession.NONE).orElseThrow();
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
        if (v == null || v.getVillagerXp() > 0 || v.getVillagerData().profession().is(VillagerProfession.NITWIT)) {
            return;
        }
        v.setVillagerData(v.getVillagerData().withProfession(getWorkstationProfession()));
    }

    public boolean openTradingGUI(Player playerEntity) {
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            return false;
        }

        if (villagerEntity.isBaby()) {
            return false;
        }

        Holder<VillagerProfession> profession = villagerEntity.getVillagerData().profession();
        if (profession.is(VillagerProfession.NONE) || profession.is(VillagerProfession.NITWIT)) {
            return false;
        }

        if (villagerEntity.isTrading()) {
            return false;
        }

        if (level == null || level.isClientSide()) {
            return true;
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
        if(!(level instanceof ServerLevel serverLevel)){
            return;
        }

        if (advanceAge()) {
            sync();
        }
        setChanged();

        VillagerBlockBase.playRandomVillagerSound(serverLevel, getBlockPos(), SoundEvents.VILLAGER_AMBIENT);

        if (!v.isTrading()) {
            if (v.increaseProfessionLevelOnUpdate) {
                v.increaseMerchantCareer(serverLevel);
                v.increaseProfessionLevelOnUpdate = false;
                sync();
            }

            if (level.getGameTime() - getLastRestock() > nextRestock && v.getVillagerData().profession().is(getWorkstationProfession())) {
                restock();
                nextRestock = calculateNextRestock();
            }
        }
    }

    protected long calculateNextRestock() {
        return EasyVillagersMod.SERVER_CONFIG.traderMinRestockTime.get() + level.random.nextInt(Math.max(EasyVillagersMod.SERVER_CONFIG.traderMaxRestockTime.get() - EasyVillagersMod.SERVER_CONFIG.traderMinRestockTime.get(), 1));
    }

    protected void restock() {
        try {
            EasyVillagerEntity villagerEntity = getVillagerEntity();
            if (villagerEntity == null) {
                return;
            }
            villagerEntity.restock();
            SoundEvent workSound = villagerEntity.getVillagerData().profession().value().workSound();
            if (workSound != null) {
                VillagerBlockBase.playVillagerSound(level, getBlockPos(), workSound);
            }
        } catch (Exception e) {
            EasyVillagersMod.LOGGER.error("Error restocking villager", e);
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
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        if (hasWorkstation()) {
            valueOutput.putString("Workstation", BuiltInRegistries.BLOCK.getKey(workstation).toString());
        }
        valueOutput.putLong("NextRestock", nextRestock);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        Optional<Block> optionalWorkstation = valueInput.read("Workstation", Identifier.CODEC).map(r -> BuiltInRegistries.BLOCK.get(r).map(Holder.Reference::value).orElse(Blocks.AIR));
        if (optionalWorkstation.isPresent()) {
            workstation = optionalWorkstation.get();
        } else {
            removeWorkstation();
        }
        nextRestock = valueInput.getLongOr("NextRestock", 0L);
        super.loadAdditional(valueInput);
    }

}
