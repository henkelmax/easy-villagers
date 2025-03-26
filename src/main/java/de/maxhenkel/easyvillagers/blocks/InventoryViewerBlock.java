package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.blockentity.SimpleBlockEntityTicker;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.InventoryViewerTileentity;
import de.maxhenkel.easyvillagers.datacomponents.VillagerBlockEntityData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.InventoryViewerContainer;
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
import java.util.function.Consumer;

public class InventoryViewerBlock extends VillagerBlockBase implements EntityBlock {

    public InventoryViewerBlock(Properties properties) {
        super(properties.mapColor(MapColor.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public void onTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> component) {
        super.onTooltip(stack, context, component);
        InventoryViewerTileentity trader = VillagerBlockEntityData.getAndStoreBlockEntity(stack, context.registries(), context.level(), () -> new InventoryViewerTileentity(BlockPos.ZERO, ModBlocks.INVENTORY_VIEWER.get().defaultBlockState()));
        EasyVillagerEntity villager = trader.getVillagerEntity();
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
                serverPlayer.openMenu(new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(state.getBlock().getDescriptionId());
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                        return new InventoryViewerContainer(id, playerInventory, inventoryViewer, ContainerLevelAccess.create(worldIn, pos));
                    }
                }, packetBuffer -> packetBuffer.writeBlockPos(inventoryViewer.getBlockPos()));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState state, BlockEntityType<T> type) {
        return new SimpleBlockEntityTicker<>();
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }

}
