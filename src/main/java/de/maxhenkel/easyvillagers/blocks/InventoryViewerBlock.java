package de.maxhenkel.easyvillagers.blocks;


import de.maxhenkel.easyvillagers.utils.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.InventoryViewerContainer;
import de.maxhenkel.easyvillagers.items.BlockItemDataCache;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public class InventoryViewerBlock extends VillagerBlockBase {

    public InventoryViewerBlock(Properties properties) {
        super(properties.mapColor(MapColor.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public void onTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> component) {
        super.onTooltip(stack, context, component);
        net.minecraft.core.HolderLookup.Provider registries = context.registries();
        
        InventoryViewerTileentity invViewer = BlockItemDataCache.get(registries, stack, InventoryViewerTileentity.class);
        if (invViewer == null) {
            return;
        }
        EasyVillagerEntity villager = invViewer.getVillagerEntity();
        if (villager != null) {
            component.accept(villager.getAdvancedName());
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack heldItem, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof InventoryViewerTileentity)) {
            return super.useItemOn(heldItem, state, worldIn, pos, player, handIn, hit);
        }
        InventoryViewerTileentity inventoryViewer = (InventoryViewerTileentity) tileEntity;
        if (!inventoryViewer.hasVillager() && heldItem.getItem() instanceof VillagerItem) {
            inventoryViewer.setVillager(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_CELEBRATE);
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && inventoryViewer.hasVillager()) {
            ItemStack stack = inventoryViewer.removeVillager();
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
        } else if (inventoryViewer.hasVillager()) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider<net.minecraft.core.BlockPos>() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(state.getBlock().getDescriptionId());
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                        return new InventoryViewerContainer(id, playerInventory, inventoryViewer, ContainerLevelAccess.create(worldIn, pos));
                    }

                    @Override
                    public net.minecraft.core.BlockPos getScreenOpeningData(ServerPlayer player) {
                        return pos;
                    }
                });
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InventoryViewerTileentity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hitResult) {
        return useItemOn(ItemStack.EMPTY, state, worldIn, pos, player, net.minecraft.world.InteractionHand.MAIN_HAND, hitResult);
    }

}


