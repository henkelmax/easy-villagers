package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.corelib.blockentity.SimpleBlockEntityTicker;
import de.maxhenkel.corelib.client.CustomRendererBlockItem;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.datacomponents.VillagerBlockEntityData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.BreederContainer;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import de.maxhenkel.easyvillagers.items.render.BreederItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
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

public class BreederBlock extends VillagerBlockBase implements EntityBlock, IItemBlock {

    public BreederBlock() {
        super(Properties.of().mapColor(MapColor.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public Item toItem() {
        return new CustomRendererBlockItem(this, new Item.Properties()) {
            @OnlyIn(Dist.CLIENT)
            @Override
            public ItemRenderer createItemRenderer() {
                return new BreederItemRenderer();
            }
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        BreederTileentity trader = VillagerBlockEntityData.getAndStoreBlockEntity(stack, context.registries(), null, () -> new BreederTileentity(BlockPos.ZERO, ModBlocks.BREEDER.get().defaultBlockState()));
        EasyVillagerEntity villager1 = trader.getVillagerEntity1();
        if (villager1 != null) {
            components.add(villager1.getAdvancedName());
        }
        EasyVillagerEntity villager2 = trader.getVillagerEntity2();
        if (villager2 != null) {
            components.add(villager2.getAdvancedName());
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof BreederTileentity)) {
            return super.useItemOn(heldItem, state, worldIn, pos, player, handIn, hit);
        }
        BreederTileentity breeder = (BreederTileentity) tileEntity;

        if (!breeder.hasVillager1() && heldItem.getItem() instanceof VillagerItem) {
            breeder.setVillager1(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_YES);
            return ItemInteractionResult.SUCCESS;
        } else if (!breeder.hasVillager2() && heldItem.getItem() instanceof VillagerItem) {
            breeder.setVillager2(heldItem.copy());
            ItemUtils.decrItemStack(heldItem, player);
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_YES);
            return ItemInteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && breeder.hasVillager2()) {
            ItemStack stack = breeder.removeVillager2();
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, stack);
            } else {
                if (!player.getInventory().add(stack)) {
                    Direction direction = state.getValue(FarmerBlock.FACING);
                    Containers.dropItemStack(worldIn, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, stack);
                }
            }
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_CELEBRATE);
            return ItemInteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && breeder.hasVillager1()) {
            ItemStack stack = breeder.removeVillager1();
            if (heldItem.isEmpty()) {
                player.setItemInHand(handIn, stack);
            } else {
                if (!player.getInventory().add(stack)) {
                    Direction direction = state.getValue(FarmerBlock.FACING);
                    Containers.dropItemStack(worldIn, direction.getStepX() + pos.getX() + 0.5D, pos.getY() + 0.5D, direction.getStepZ() + pos.getZ() + 0.5D, stack);
                }
            }
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_CELEBRATE);
            return ItemInteractionResult.SUCCESS;
        } else {
            player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable(state.getBlock().getDescriptionId());
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                    return new BreederContainer(id, playerInventory, breeder.getFoodInventory(), breeder.getOutputInventory(), ContainerLevelAccess.create(worldIn, pos));
                }
            });
            return ItemInteractionResult.SUCCESS;
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
        return new BreederTileentity(blockPos, blockState);
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
