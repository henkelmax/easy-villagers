package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible;
import de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration;

import de.maxhenkel.easyvillagers.blocks.tileentity.IServerTickableBlockEntity;
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

public abstract class TraderTileentityBase extends VillagerTileentity implements IServerTickableBlockEntity, de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible {

    protected Block workstation;
    protected long nextRestock;
    protected TRSlotConfiguration trSlotConfig;

    public TraderTileentityBase(BlockEntityType<?> type, BlockState defaultState, BlockPos pos, BlockState state) {
        super(type, defaultState, pos, state);
        workstation = Blocks.AIR;
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
            trSlotConfig = new TRSlotConfiguration();
        }
    }

    @Override
    public Object getSlotConfiguration() {
        return trSlotConfig;
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
        EasyVillagersMod.LOGGER.info("getWorkstationProfession workstation: " + workstation + ", poiTypeHolder present: " + poiTypeHolder.isPresent());
        if (poiTypeHolder.isEmpty()) {
            return BuiltInRegistries.VILLAGER_PROFESSION.get(VillagerProfession.NONE).orElseThrow();
        }

        Holder<PoiType> poiType = poiTypeHolder.get();
        EasyVillagersMod.LOGGER.info("poiType: " + poiType.unwrapKey().map(k -> k.toString()).orElse("NO_KEY"));
        for (VillagerProfession profession : BuiltInRegistries.VILLAGER_PROFESSION) {
            if (profession.heldJobSite().test(poiType)) {
                EasyVillagersMod.LOGGER.info("Matched profession: " + BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession));
                return BuiltInRegistries.VILLAGER_PROFESSION.wrapAsHolder(profession);
            }
        }

        EasyVillagersMod.LOGGER.info("No profession matched!");
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
        
        Holder<VillagerProfession> currentProfession = v.getVillagerData().profession();
        Holder<VillagerProfession> newProfession = getWorkstationProfession();
        
        if (currentProfession.value() == newProfession.value()) {
            return;
        }

        v.setVillagerData(v.getVillagerData().withProfession(newProfession));
        
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            v.getOffers().clear();
            v.forceUpdateTrades(serverLevel);
        }
        de.maxhenkel.easyvillagers.datacomponents.VillagerData.applyToItem(this.villager, v);
    }

    public boolean openTradingGUI(Player playerEntity) {
        EasyVillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
            EasyVillagersMod.LOGGER.info("openTradingGUI failed: villagerEntity is null");
            return false;
        }

        if (villagerEntity.isBaby()) {
            EasyVillagersMod.LOGGER.info("openTradingGUI failed: villager is baby");
            return false;
        }

        Holder<VillagerProfession> profession = villagerEntity.getVillagerData().profession();
        if (profession.is(VillagerProfession.NONE) || profession.is(VillagerProfession.NITWIT)) {
            EasyVillagersMod.LOGGER.info("openTradingGUI failed: profession is NONE or NITWIT (" + profession.unwrapKey().map(k -> k.toString()).orElse("NO_KEY") + ")");
            return false;
        }
        if (level == null || level.isClientSide()) {
            return true;
        }

        EasyVillagersMod.LOGGER.info("openTradingGUI success: Opening GUI for " + profession.unwrapKey().map(k -> k.toString()).orElse("NO_KEY") + " with xp " + villagerEntity.getVillagerXp());
        villagerEntity.setPos(getBlockPos().getX() + 0.5D, getBlockPos().getY() + 1D, getBlockPos().getZ() + 0.5D);
        villagerEntity.setTradingPlayer(playerEntity);
        villagerEntity.openTradingScreen(playerEntity, villagerEntity.getDisplayName(), villagerEntity.getVillagerData().level());
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

        if (v.increaseProfessionLevelOnUpdate) {
            v.setVillagerData(v.getVillagerData().withLevel(v.getVillagerData().level() + 1));
            v.forceUpdateTrades(serverLevel);
            v.increaseProfessionLevelOnUpdate = false;
            
            Player tradingPlayer = v.getTradingPlayer();
            if (tradingPlayer instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                serverPlayer.sendMerchantOffers(serverPlayer.containerMenu.containerId, v.getOffers(), v.getVillagerData().level(), v.getVillagerXp(), v.showProgressBar(), v.canRestock());
            }
            sync();
        }

        if (!v.isTrading()) {
            if (level.getGameTime() - getLastRestock() > nextRestock && v.getVillagerData().profession().value() == getWorkstationProfession().value()) {
                restock();
                nextRestock = calculateNextRestock();
            }
        }
    }

    protected long calculateNextRestock() {
        return EasyVillagersMod.CONFIG.server.traderMinRestockTime.get() + level.getRandom().nextInt(Math.max(EasyVillagersMod.CONFIG.server.traderMaxRestockTime.get() - EasyVillagersMod.CONFIG.server.traderMinRestockTime.get(), 1));
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
        if (trSlotConfig != null) {
            valueOutput.store("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC, trSlotConfig.serialize());
        }
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        String id = valueInput.getStringOr("Workstation", "");
        if (!id.isEmpty()) {
            Identifier identifier = Identifier.tryParse(id);
            if (identifier != null) {
                workstation = BuiltInRegistries.BLOCK.get(identifier).map(Holder.Reference::value).orElse(Blocks.AIR);
            }
        } else {
            removeWorkstation();
        }
        nextRestock = valueInput.getLongOr("NextRestock", 0L);
        if (trSlotConfig != null && valueInput.contains("TRSlotConfig")) {
            valueInput.read("TRSlotConfig", net.minecraft.nbt.CompoundTag.CODEC).ifPresent(tag -> trSlotConfig.deserialize(tag)); // No, read directly from ValueInput is hard, we can use NBT
        }
        super.loadAdditional(valueInput);
    }

}
