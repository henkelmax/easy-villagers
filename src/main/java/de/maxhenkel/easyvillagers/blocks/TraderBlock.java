package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import de.maxhenkel.easyvillagers.items.render.TraderItemRenderer;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TraderBlock extends Block implements ITileEntityProvider, IItemBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TraderBlock() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(2.5F).sound(SoundType.METAL).notSolid());
        setRegistryName(new ResourceLocation(Main.MODID, "trader"));
        setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().group(ItemGroup.MISC).setISTER(() -> TraderItemRenderer::new)).setRegistryName(getRegistryName());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof TraderTileentity)) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
        TraderTileentity trader = (TraderTileentity) tileEntity;
        if (!trader.hasVillager() && heldItem.getItem() instanceof VillagerItem) {
            trader.setVillager(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            if (trader.hasWorkstation()) {
                VillagerEntity villagerEntity = trader.getVillagerEntity();
                if (villagerEntity != null) {
                    playVillagerSound(worldIn, pos, villagerEntity.getVillagerData().getProfession().getSound());
                }
            } else {
                playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_CELEBRATE);
            }
            return ActionResultType.SUCCESS;
        } else if (!trader.hasWorkstation() && heldItem.getItem() instanceof BlockItem && trader.isValidBlock(((BlockItem) heldItem.getItem()).getBlock())) {
            Block block = ((BlockItem) heldItem.getItem()).getBlock();
            trader.setWorkstation(block);
            ItemUtils.decrItemStack(heldItem, player);
            VillagerEntity villagerEntity = trader.getVillagerEntity();
            if (villagerEntity != null) {
                playVillagerSound(worldIn, pos, villagerEntity.getVillagerData().getProfession().getSound());
            }
            SoundType type = block.getSoundType(block.getDefaultState());
            worldIn.playSound(null, pos, type.getPlaceSound(), SoundCategory.BLOCKS, type.getVolume(), type.getPitch());
            return ActionResultType.SUCCESS;
        } else if (player.isSneaking() && trader.hasWorkstation()) {
            ItemStack blockStack = new ItemStack(trader.removeWorkstation());
            if (heldItem.isEmpty()) {
                player.setHeldItem(handIn, blockStack);
            } else {
                if (!player.inventory.addItemStackToInventory(blockStack)) {
                    Direction direction = state.get(TraderBlock.FACING);
                    InventoryHelper.spawnItemStack(worldIn, direction.getXOffset() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getZOffset() + pos.getZ() + 0.5D, blockStack);
                }
            }
            if (trader.hasVillager()) {
                playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_NO);
            }
            return ActionResultType.SUCCESS;
        } else if (player.isSneaking() && trader.hasVillager()) {
            ItemStack stack = trader.removeVillager();
            if (heldItem.isEmpty()) {
                player.setHeldItem(handIn, stack);
            } else {
                if (!player.inventory.addItemStackToInventory(stack)) {
                    Direction direction = state.get(TraderBlock.FACING);
                    InventoryHelper.spawnItemStack(worldIn, direction.getXOffset() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getZOffset() + pos.getZ() + 0.5D, stack);
                }
            }
            playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_CELEBRATE);
            return ActionResultType.SUCCESS;
        } else if (trader.openTradingGUI(player)) {
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    public static void playVillagerSound(World world, BlockPos pos, SoundEvent soundEvent) {
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundCategory.NEUTRAL, 1F, 1F);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TraderTileentity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1F;
    }

}
