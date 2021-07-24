package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.ModItemGroups;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.gui.AutoTraderContainer;
import de.maxhenkel.easyvillagers.items.render.AutoTraderItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class AutoTraderBlock extends TraderBlockBase {

    public AutoTraderBlock() {
        setRegistryName(new ResourceLocation(Main.MODID, "auto_trader"));
    }

    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().tab(ModItemGroups.TAB_EASY_VILLAGERS)) {
            @Override
            public void initializeClient(Consumer<IItemRenderProperties> consumer) {
                consumer.accept(new IItemRenderProperties() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                        return new AutoTraderItemRenderer();
                    }
                });
            }
        }.setRegistryName(getRegistryName());
    }

    @Override
    protected boolean openGUI(TraderTileentityBase trader, Player player) {
        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("block.easy_villagers.auto_trader");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                AutoTraderTileentity autoTrader = (AutoTraderTileentity) trader;
                return new AutoTraderContainer(id, inv, autoTrader, autoTrader.getInputInventory(), autoTrader.getOutputInventory());
            }
        });
        return true;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState state, BlockEntityType<T> type) {
        return (level, blockPos, blockState, t) -> {
            if (t instanceof AutoTraderTileentity te) {
                if (!level1.isClientSide) {
                    te.tickServer();
                }
                te.tick();
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AutoTraderTileentity(blockPos, blockState);
    }
}