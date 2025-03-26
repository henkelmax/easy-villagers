package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerBlockEntityData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.AutoTraderContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class AutoTraderBlock extends TraderBlockBase {

    public AutoTraderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> component) {
        AutoTraderTileentity trader = VillagerBlockEntityData.getAndStoreBlockEntity(stack, context.registries(), context.level(), () -> new AutoTraderTileentity(BlockPos.ZERO, ModBlocks.AUTO_TRADER.get().defaultBlockState()));
        EasyVillagerEntity villager = trader.getVillagerEntity();
        if (villager != null) {
            component.accept(villager.getAdvancedName());
        }
    }

    @Override
    protected boolean openGUI(TraderTileentityBase trader, Player player, Level level, BlockPos pos) {
        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("block.easy_villagers.auto_trader");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                AutoTraderTileentity autoTrader = (AutoTraderTileentity) trader;
                return new AutoTraderContainer(id, inv, autoTrader, autoTrader.getInputInventory(), autoTrader.getOutputInventory(), ContainerLevelAccess.create(level, pos));
            }
        });
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AutoTraderTileentity(blockPos, blockState);
    }
}