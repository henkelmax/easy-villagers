package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.IItemBlock;
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
import net.minecraft.world.item.Item;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public abstract class TraderBlockBase extends VillagerBlockBase implements EntityBlock, IItemBlock {

    public TraderBlockBase() {
        super(Properties.of(Material.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public abstract Item toItem();

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof TraderTileentityBase)) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
        TraderTileentityBase trader = (TraderTileentityBase) tileEntity;
        if (!trader.hasVillager() && heldItem.getItem() instanceof VillagerItem) {
            trader.setVillager(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            if (trader.hasWorkstation()) {
                Villager villagerEntity = trader.getVillagerEntity();
                if (villagerEntity != null) {
                    playWorkstationSound(worldIn, pos, trader);
                }
            } else {
                playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_CELEBRATE);
            }
            return InteractionResult.SUCCESS;
        } else if (!trader.hasWorkstation() && heldItem.getItem() instanceof BlockItem && trader.isValidBlock(((BlockItem) heldItem.getItem()).getBlock())) {
            Block block = ((BlockItem) heldItem.getItem()).getBlock();
            trader.setWorkstation(block);
            ItemUtils.decrItemStack(heldItem, player);
            Villager villagerEntity = trader.getVillagerEntity();
            if (villagerEntity != null) {
                playWorkstationSound(worldIn, pos, trader);
            }
            SoundType type = block.getSoundType(block.defaultBlockState());
            worldIn.playSound(null, pos, type.getPlaceSound(), SoundSource.BLOCKS, type.getVolume(), type.getPitch());
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && trader.hasVillager()) {
            ItemStack stack = trader.removeVillager();
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, stack);
            } else {
                if (!player.getInventory().add(stack)) {
                    Direction direction = state.getValue(TraderBlockBase.FACING);
                    Containers.dropItemStack(worldIn, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, stack);
                }
            }
            playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_CELEBRATE);
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && trader.hasWorkstation()) {
            ItemStack blockStack = new ItemStack(trader.removeWorkstation());
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, blockStack);
            } else {
                if (!player.getInventory().add(blockStack)) {
                    Direction direction = state.getValue(TraderBlockBase.FACING);
                    Containers.dropItemStack(worldIn, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, blockStack);
                }
            }
            if (trader.hasVillager()) {
                playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_NO);
            }
            return InteractionResult.SUCCESS;
        } else if (openGUI(trader, player)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    protected abstract boolean openGUI(TraderTileentityBase trader, Player player);

    protected void playWorkstationSound(Level world, BlockPos pos, TraderTileentityBase trader) {
        Villager villagerEntity = trader.getVillagerEntity();
        if (villagerEntity != null) {
            if (trader.getWorkstationProfession().equals(villagerEntity.getVillagerData().getProfession())) {
                playVillagerSound(world, pos, trader.getWorkstationProfession().getWorkSound());
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
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }

}
