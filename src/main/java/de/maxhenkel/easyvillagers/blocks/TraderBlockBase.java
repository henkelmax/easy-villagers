package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.blockentity.SimpleBlockEntityTicker;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public abstract class TraderBlockBase extends VillagerBlockBase implements EntityBlock {

    public TraderBlockBase(Properties properties) {
        super(properties.mapColor(MapColor.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (!(tileEntity instanceof TraderTileentityBase)) {
            return super.useItemOn(heldItem, state, level, pos, player, handIn, hit);
        }
        TraderTileentityBase trader = (TraderTileentityBase) tileEntity;
        if (!trader.hasVillager() && heldItem.getItem() instanceof VillagerItem) {
            trader.setVillager(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            if (trader.hasWorkstation()) {
                Villager villagerEntity = trader.getVillagerEntity();
                if (villagerEntity != null) {
                    playWorkstationSound(level, pos, trader);
                }
            } else {
                playVillagerSound(level, pos, SoundEvents.VILLAGER_CELEBRATE);
            }
            return InteractionResult.SUCCESS;
        } else if (!trader.hasWorkstation() && heldItem.getItem() instanceof BlockItem && trader.isValidBlock(((BlockItem) heldItem.getItem()).getBlock())) {
            Block block = ((BlockItem) heldItem.getItem()).getBlock();
            trader.setWorkstation(block);
            ItemUtils.decrItemStack(heldItem, player);
            Villager villagerEntity = trader.getVillagerEntity();
            if (villagerEntity != null) {
                playWorkstationSound(level, pos, trader);
            }
            SoundType type = block.defaultBlockState().getSoundType(level, pos, player);
            level.playSound(null, pos, type.getPlaceSound(), SoundSource.BLOCKS, type.getVolume(), type.getPitch());
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && trader.hasVillager()) {
            ItemStack stack = trader.removeVillager();
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, stack);
            } else {
                if (!player.getInventory().add(stack)) {
                    Direction direction = state.getValue(TraderBlockBase.FACING);
                    Containers.dropItemStack(level, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, stack);
                }
            }
            playVillagerSound(level, pos, SoundEvents.VILLAGER_CELEBRATE);
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && trader.hasWorkstation()) {
            ItemStack blockStack = new ItemStack(trader.removeWorkstation());
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, blockStack);
            } else {
                if (!player.getInventory().add(blockStack)) {
                    Direction direction = state.getValue(TraderBlockBase.FACING);
                    Containers.dropItemStack(level, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, blockStack);
                }
            }
            if (trader.hasVillager()) {
                playVillagerSound(level, pos, SoundEvents.VILLAGER_NO);
            }
            return InteractionResult.SUCCESS;
        } else if (openGUI(trader, player, level, pos)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    protected abstract boolean openGUI(TraderTileentityBase trader, Player player, Level level, BlockPos pos);

    protected void playWorkstationSound(Level world, BlockPos pos, TraderTileentityBase trader) {
        Villager villagerEntity = trader.getVillagerEntity();
        if (villagerEntity != null) {
            if (trader.getWorkstationProfession().equals(villagerEntity.getVillagerData().profession().value())) {
                playVillagerSound(world, pos, trader.getWorkstationProfession().value().workSound());
            } else {
                playVillagerSound(world, pos, SoundEvents.VILLAGER_NO);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState state, BlockEntityType<T> type) {
        return new SimpleBlockEntityTicker<>();
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }

}
