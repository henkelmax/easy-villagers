package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.blockentity.SimpleBlockEntityTicker;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity;
import de.maxhenkel.easyvillagers.datacomponents.VillagerBlockEntityData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.OutputContainer;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class FarmerBlock extends VillagerBlockBase implements EntityBlock {

    public FarmerBlock(Properties properties) {
        super(properties.mapColor(MapColor.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        FarmerTileentity trader = VillagerBlockEntityData.getAndStoreBlockEntity(stack, context.registries(), context.level(), () -> new FarmerTileentity(BlockPos.ZERO, ModBlocks.FARMER.get().defaultBlockState()));
        EasyVillagerEntity villager = trader.getVillagerEntity();
        if (villager != null) {
            components.add(villager.getAdvancedName());
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof FarmerTileentity)) {
            return super.useItemOn(heldItem, state, worldIn, pos, player, handIn, hit);
        }
        FarmerTileentity farmer = (FarmerTileentity) tileEntity;
        if (!farmer.hasVillager() && heldItem.getItem() instanceof VillagerItem) {
            farmer.setVillager(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_YES);
            return InteractionResult.SUCCESS;
        } else if (farmer.getCrop() == null && farmer.isValidSeed(heldItem.getItem())) {
            Item seed = heldItem.getItem();
            farmer.setCrop(seed);
            ItemUtils.decrItemStack(heldItem, player);
            Villager villagerEntity = farmer.getVillagerEntity();
            if (villagerEntity != null) {
                VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_WORK_FARMER);
            }
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.CROP_PLANTED);
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && farmer.getCrop() != null) {
            ItemStack blockStack = new ItemStack(farmer.removeSeed());
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, blockStack);
            } else {
                if (!player.getInventory().add(blockStack)) {
                    Direction direction = state.getValue(FarmerBlock.FACING);
                    Containers.dropItemStack(worldIn, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, blockStack);
                }
            }
            if (farmer.hasVillager()) {
                VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_NO);
            }
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && farmer.hasVillager()) {
            ItemStack stack = farmer.removeVillager();
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, stack);
            } else {
                if (!player.getInventory().add(stack)) {
                    Direction direction = state.getValue(FarmerBlock.FACING);
                    Containers.dropItemStack(worldIn, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, stack);
                }
            }
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_CELEBRATE);
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable(state.getBlock().getDescriptionId());
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                    return new OutputContainer(id, playerInventory, farmer.getOutputInventory(), ContainerLevelAccess.create(worldIn, pos), ModBlocks.FARMER::get);
                }
            });
            return InteractionResult.SUCCESS;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState state, BlockEntityType<T> type) {
        return new SimpleBlockEntityTicker<>();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FarmerTileentity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }

}
