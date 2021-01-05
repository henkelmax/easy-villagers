package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.ModItemGroups;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.gui.BreederContainer;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import de.maxhenkel.easyvillagers.items.render.BreederItemRenderer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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

public class BreederBlock extends VillagerBlockBase implements ITileEntityProvider, IItemBlock {

    public BreederBlock() {
        super(Properties.create(Material.IRON).hardnessAndResistance(2.5F).sound(SoundType.METAL).notSolid().setLightLevel(value -> 15));
        setRegistryName(new ResourceLocation(Main.MODID, "breeder"));
    }

    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().group(ModItemGroups.TAB_EASY_VILLAGERS).setISTER(() -> BreederItemRenderer::new)).setRegistryName(getRegistryName());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof BreederTileentity)) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
        BreederTileentity breeder = (BreederTileentity) tileEntity;

        if (!breeder.hasVillager1() && heldItem.getItem() instanceof VillagerItem) {
            breeder.setVillager1(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_YES);
            return ActionResultType.SUCCESS;
        } else if (!breeder.hasVillager2() && heldItem.getItem() instanceof VillagerItem) {
            breeder.setVillager2(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_YES);
            return ActionResultType.SUCCESS;
        } else if (player.isSneaking() && breeder.hasVillager2()) {
            ItemStack stack = breeder.removeVillager2();
            if (heldItem.isEmpty()) {
                player.setHeldItem(handIn, stack);
            } else {
                if (!player.inventory.addItemStackToInventory(stack)) {
                    Direction direction = state.get(FarmerBlock.FACING);
                    InventoryHelper.spawnItemStack(worldIn, direction.getXOffset() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getZOffset() + pos.getZ() + 0.5D, stack);
                }
            }
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_CELEBRATE);
            return ActionResultType.SUCCESS;
        } else if (player.isSneaking() && breeder.hasVillager1()) {
            ItemStack stack = breeder.removeVillager1();
            if (heldItem.isEmpty()) {
                player.setHeldItem(handIn, stack);
            } else {
                if (!player.inventory.addItemStackToInventory(stack)) {
                    Direction direction = state.get(FarmerBlock.FACING);
                    InventoryHelper.spawnItemStack(worldIn, direction.getXOffset() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getZOffset() + pos.getZ() + 0.5D, stack);
                }
            }
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.ENTITY_VILLAGER_CELEBRATE);
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
                    return new BreederContainer(id, playerInventory, breeder.getFoodInventory(), breeder.getOutputInventory());
                }
            });
            return ActionResultType.SUCCESS;
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new BreederTileentity();
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
