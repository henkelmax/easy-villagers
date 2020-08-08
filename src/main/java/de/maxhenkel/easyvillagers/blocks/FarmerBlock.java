package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import de.maxhenkel.easyvillagers.gui.OutputContainer;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import de.maxhenkel.easyvillagers.items.render.FarmerItemRenderer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FarmerBlock extends HorizontalRotatableBlock implements ITileEntityProvider, IItemBlock {

    public FarmerBlock() {
        super(Properties.create(Material.IRON).hardnessAndResistance(2.5F).sound(SoundType.METAL).notSolid());
        setRegistryName(new ResourceLocation(Main.MODID, "farmer"));
    }

    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> FarmerItemRenderer::new)).setRegistryName(getRegistryName());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof FarmerTileentity)) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
        FarmerTileentity farmer = (FarmerTileentity) tileEntity;
        if (!farmer.hasVillager() && heldItem.getItem() instanceof VillagerItem) {
            farmer.setVillager(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            TraderBlock.playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_YES);
            return ActionResultType.SUCCESS;
        } else if (farmer.getCrop() == null && farmer.isValidSeed(heldItem.getItem())) {
            Item seed = heldItem.getItem();
            farmer.setCrop(seed);
            ItemUtils.decrItemStack(heldItem, player);
            VillagerEntity villagerEntity = farmer.getVillagerEntity();
            if (villagerEntity != null) {
                TraderBlock.playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_WORK_FARMER);
            }
            TraderBlock.playVillagerSound(worldIn, pos, SoundEvents.ITEM_CROP_PLANT);
            return ActionResultType.SUCCESS;
        } else if (player.isSneaking() && farmer.getCrop() != null) {
            ItemStack blockStack = new ItemStack(farmer.removeSeed());
            if (heldItem.isEmpty()) {
                player.setHeldItem(handIn, blockStack);
            } else {
                if (!player.inventory.addItemStackToInventory(blockStack)) {
                    Direction direction = state.get(FarmerBlock.FACING);
                    InventoryHelper.spawnItemStack(worldIn, direction.getXOffset() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getZOffset() + pos.getZ() + 0.5D, blockStack);
                }
            }
            if (farmer.hasVillager()) {
                TraderBlock.playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_NO);
            }
            return ActionResultType.SUCCESS;
        } else if (player.isSneaking() && farmer.hasVillager()) {
            ItemStack stack = farmer.removeVillager();
            if (heldItem.isEmpty()) {
                player.setHeldItem(handIn, stack);
            } else {
                if (!player.inventory.addItemStackToInventory(stack)) {
                    Direction direction = state.get(FarmerBlock.FACING);
                    InventoryHelper.spawnItemStack(worldIn, direction.getXOffset() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getZOffset() + pos.getZ() + 0.5D, stack);
                }
            }
            TraderBlock.playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_CELEBRATE);
            return ActionResultType.SUCCESS;
        } else {
            player.openContainer(new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent(state.getBlock().getTranslationKey());
                }

                @Nullable
                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    return new OutputContainer(id, playerInventory, farmer.getOutputInventory());
                }
            });
            return ActionResultType.SUCCESS;
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new FarmerTileentity();
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
