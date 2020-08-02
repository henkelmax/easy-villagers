package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.corelib.CachedValue;
import de.maxhenkel.corelib.entity.EntityUtils;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TraderTileentity extends TileEntity implements ITickableTileEntity {

    private ItemStack villager;
    private VillagerEntity villagerEntity;
    private Block workstation;
    private long nextRestock;
    private boolean fakeWorld;

    private static final CachedValue<Field> LAST_RESTOCK = new CachedValue<>(() -> ObfuscationReflectionHelper.findField(VillagerEntity.class, "field_213785_bP"));
    private static final CachedValue<Method> RESTOCK = new CachedValue<>(() -> ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_223718_eH"));
    private static final CachedValue<Field> LEVELED_UP = new CachedValue<>(() -> ObfuscationReflectionHelper.findField(VillagerEntity.class, "field_213777_bF"));
    private static final CachedValue<Method> LEVEL_UP = new CachedValue<>(() -> ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_175554_cu"));
    private static final CachedValue<Method> DISPLAY_MERCHANT_GUI = new CachedValue<>(() -> ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "func_213740_f", PlayerEntity.class));

    public TraderTileentity() {
        super(ModTileEntities.TRADER);
        villager = ItemStack.EMPTY;
        workstation = Blocks.AIR;
    }

    public ItemStack getVillager() {
        return villager;
    }

    public boolean hasVillager() {
        return !villager.isEmpty();
    }

    public VillagerEntity getVillagerEntity() {
        if (villagerEntity == null && !villager.isEmpty()) {
            villagerEntity = ModItems.VILLAGER.getVillager(world, villager);
        }
        return villagerEntity;
    }

    public Block getWorkstation() {
        return workstation;
    }

    public boolean hasWorkstation() {
        return workstation != Blocks.AIR;
    }

    public void setVillager(ItemStack villager) {
        this.villager = villager;

        if (villager.isEmpty()) {
            villagerEntity = null;
        } else {
            villagerEntity = ModItems.VILLAGER.getVillager(world, villager);
            if (hasWorkstation()) {
                fixProfession();
            }
        }
        markDirty();
        sync();
    }

    private void fixProfession() {
        VillagerEntity v = getVillagerEntity();
        if (v == null || v.getXp() > 0) {
            return;
        }
        v.setVillagerData(v.getVillagerData().withProfession(getWorkstationProfession()));
    }

    public boolean isValidBlock(Block block) {
        return PointOfInterestType.forState(block.getDefaultState()).isPresent();
    }

    public VillagerProfession getWorkstationProfession() {
        return PointOfInterestType.forState(workstation.getDefaultState()).flatMap(pointOfInterestType -> ForgeRegistries.PROFESSIONS.getValues().stream().filter(villagerProfession -> villagerProfession.getPointOfInterest() == pointOfInterestType).findFirst()).orElse(VillagerProfession.NONE);
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

    public ItemStack removeVillager() {
        ItemStack v = villager;
        setVillager(ItemStack.EMPTY);
        return v;
    }

    public boolean openTradingGUI(PlayerEntity playerEntity) {
        VillagerEntity villagerEntity = getVillagerEntity();
        if (villagerEntity == null) {
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

        if (world.getGameTime() % 20 == 0 && world.rand.nextInt(20) == 0) {
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

            if (world.getGameTime() - getLastRestock() > nextRestock && villagerEntity.getVillagerData().getProfession().equals(getWorkstationProfession())) {
                restock();
                nextRestock = calculateNextRestock();
            }
        }
    }

    private long calculateNextRestock() {
        return 20 * 60 + world.rand.nextInt(20 * 60 * 2);
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

    public void setFakeWorld(World w) {
        world = w;
        fakeWorld = true;
    }

    public boolean isFakeWorld() {
        return fakeWorld;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (hasVillager()) {
            CompoundNBT comp = new CompoundNBT();
            if (villagerEntity != null) {
                ModItems.VILLAGER.setVillager(villager, villagerEntity);
            }
            villager.write(comp);
            compound.put("Villager", comp);
        }
        if (hasWorkstation()) {
            compound.putString("Workstation", workstation.getRegistryName().toString());
        }
        compound.putLong("NextRestock", nextRestock);
        return super.write(compound);
    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        if (compound.contains("Villager")) {
            CompoundNBT comp = compound.getCompound("Villager");
            villager = ItemStack.read(comp);
            villagerEntity = null;
        }
        if (compound.contains("Workstation")) {
            workstation = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("Workstation")));
        }
        nextRestock = compound.getLong("NextRestock");
        super.func_230337_a_(state, compound);
    }

    private void sync() {
        if (world instanceof ServerWorld) {
            EntityUtils.forEachPlayerAround((ServerWorld) world, getPos(), 128D, this::syncContents);
        }
    }

    public void syncContents(ServerPlayerEntity player) {
        player.connection.sendPacket(getUpdatePacket());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.func_230337_a_(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

}
