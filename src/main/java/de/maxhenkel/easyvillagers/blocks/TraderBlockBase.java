package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public abstract class TraderBlockBase extends VillagerBlockBase implements ITileEntityProvider, IItemBlock {

    public TraderBlockBase() {
        super(Properties.of(Material.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public abstract Item toItem();

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof TraderTileentityBase)) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
        TraderTileentityBase trader = (TraderTileentityBase) tileEntity;
        if (!trader.hasVillager() && heldItem.getItem() instanceof VillagerItem) {
            trader.setVillager(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            if (trader.hasWorkstation()) {
                VillagerEntity villagerEntity = trader.getVillagerEntity();
                if (villagerEntity != null) {
                    playWorkstationSound(worldIn, pos, trader);
                }
            } else {
                playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_CELEBRATE);
            }
            return ActionResultType.SUCCESS;
        } else if (!trader.hasWorkstation() && heldItem.getItem() instanceof BlockItem && trader.isValidBlock(((BlockItem) heldItem.getItem()).getBlock())) {
            Block block = ((BlockItem) heldItem.getItem()).getBlock();
            trader.setWorkstation(block);
            ItemUtils.decrItemStack(heldItem, player);
            VillagerEntity villagerEntity = trader.getVillagerEntity();
            if (villagerEntity != null) {
                playWorkstationSound(worldIn, pos, trader);
            }
            SoundType type = block.getSoundType(block.defaultBlockState());
            worldIn.playSound(null, pos, type.getPlaceSound(), SoundCategory.BLOCKS, type.getVolume(), type.getPitch());
            return ActionResultType.SUCCESS;
        } else if (player.isShiftKeyDown() && trader.hasVillager()) {
            ItemStack stack = trader.removeVillager();
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, stack);
            } else {
                if (!player.inventory.add(stack)) {
                    Direction direction = state.getValue(TraderBlockBase.FACING);
                    InventoryHelper.dropItemStack(worldIn, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, stack);
                }
            }
            playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_CELEBRATE);
            return ActionResultType.SUCCESS;
        } else if (player.isShiftKeyDown() && trader.hasWorkstation()) {
            ItemStack blockStack = new ItemStack(trader.removeWorkstation());
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, blockStack);
            } else {
                if (!player.inventory.add(blockStack)) {
                    Direction direction = state.getValue(TraderBlockBase.FACING);
                    InventoryHelper.dropItemStack(worldIn, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, blockStack);
                }
            }
            if (trader.hasVillager()) {
                playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_NO);
            }
            return ActionResultType.SUCCESS;
        } else if (openGUI(trader, player)) {
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    protected abstract boolean openGUI(TraderTileentityBase trader, PlayerEntity player);

    protected void playWorkstationSound(World world, BlockPos pos, TraderTileentityBase trader) {
        VillagerEntity villagerEntity = trader.getVillagerEntity();
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
    public abstract TileEntity newBlockEntity(IBlockReader world);

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1F;
    }

}
