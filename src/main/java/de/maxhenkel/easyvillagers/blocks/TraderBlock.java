package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.datacomponents.VillagerBlockEntityData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class TraderBlock extends TraderBlockBase {

    public TraderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        TraderTileentity trader = VillagerBlockEntityData.getAndStoreBlockEntity(stack, context.registries(), context.level(), () -> new TraderTileentity(BlockPos.ZERO, ModBlocks.TRADER.get().defaultBlockState()));
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
