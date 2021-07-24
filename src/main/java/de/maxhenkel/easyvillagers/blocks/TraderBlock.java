package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.ModItemGroups;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.items.render.TraderItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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

public class TraderBlock extends TraderBlockBase {

    public TraderBlock() {
        setRegistryName(new ResourceLocation(Main.MODID, "trader"));
    }

    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().tab(ModItemGroups.TAB_EASY_VILLAGERS)) {
            @Override
            public void initializeClient(Consumer<IItemRenderProperties> consumer) {
                consumer.accept(new IItemRenderProperties() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                        return new TraderItemRenderer();
                    }
                });
            }
        }.setRegistryName(getRegistryName());
    }

    @Override
    protected boolean openGUI(TraderTileentityBase trader, Player player) {
        return trader.openTradingGUI(player);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState state, BlockEntityType<T> type) {
        if (level1.isClientSide) {
            return null;
        }
        return (level, blockPos, blockState, t) -> {
            if (t instanceof TraderTileentityBase te) {
                te.tickServer();
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TraderTileentity(blockPos, blockState);
    }

}
