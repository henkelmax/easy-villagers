package de.maxhenkel.easyvillagers.blocks;


import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.ConverterContainer;
import de.maxhenkel.easyvillagers.items.BlockItemDataCache;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
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

public class ConverterBlock extends VillagerBlockBase {

    public ConverterBlock(Properties properties) {
        super(properties.mapColor(MapColor.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public void onTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> component) {
        super.onTooltip(stack, context, component);
        net.minecraft.core.HolderLookup.Provider registries = context.registries();
        
        ConverterTileentity converter = BlockItemDataCache.get(registries, stack, ConverterTileentity.class);
        if (converter == null) {
            return;
        }
        EasyVillagerEntity villager = converter.getVillagerEntity();
        if (villager != null) {
            component.accept(villager.getAdvancedName());
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hit) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof ConverterTileentity)) {
            return super.useWithoutItem(state, worldIn, pos, player, hit);
        }
        ConverterTileentity converter = (ConverterTileentity) tileEntity;

                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider<net.minecraft.core.BlockPos>() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(state.getBlock().getDescriptionId());
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                        return new ConverterContainer(id, playerInventory, converter.getInputInventory(), converter.getOutputInventory(), ContainerLevelAccess.create(worldIn, pos));
                    }

                    @Override
                    public net.minecraft.core.BlockPos getScreenOpeningData(net.minecraft.server.level.ServerPlayer player) {
                        return pos;
                    }
                });
            }
            return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof ConverterTileentity && placer != null) {
            ConverterTileentity converter = (ConverterTileentity) tile;
            converter.setOwner(placer.getUUID());
        }
        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ConverterTileentity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }




}


