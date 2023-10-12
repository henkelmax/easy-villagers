package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.client.CustomRendererBlockItem;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.easyvillagers.ItemTileEntityCache;
import de.maxhenkel.easyvillagers.ModItemGroups;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.render.TraderItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class TraderBlock extends TraderBlockBase {

    public TraderBlock() {
    }

    @Override
    public Item toItem() {
        return new CustomRendererBlockItem(this, new Item.Properties().tab(ModItemGroups.TAB_EASY_VILLAGERS)) {
            @OnlyIn(Dist.CLIENT)
            @Override
            public ItemRenderer createItemRenderer() {
                return new TraderItemRenderer();
            }
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockGetter, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, blockGetter, components, tooltipFlag);
        TraderTileentity trader = ItemTileEntityCache.getTileEntity(stack, () -> new TraderTileentity(BlockPos.ZERO, ModBlocks.TRADER.get().defaultBlockState()));
        EasyVillagerEntity villager = trader.getVillagerEntity();
        if (villager != null) {
            components.add(villager.getAdvancedName());
        }
    }

    @Override
    protected boolean openGUI(TraderTileentityBase trader, Player player, Level level, BlockPos pos) {
        return trader.openTradingGUI(player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TraderTileentity(blockPos, blockState);
    }

}
