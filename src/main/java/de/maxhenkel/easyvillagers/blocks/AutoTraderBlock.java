package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.client.CustomRendererBlockItem;
import de.maxhenkel.corelib.client.ItemRenderer;
import de.maxhenkel.easyvillagers.ItemTileEntityCache;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.ModItemGroups;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.gui.AutoTraderContainer;
import de.maxhenkel.easyvillagers.items.render.AutoTraderItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class AutoTraderBlock extends TraderBlockBase {

    public AutoTraderBlock() {
        setRegistryName(new ResourceLocation(Main.MODID, "auto_trader"));
    }

    @Override
    public Item toItem() {
        return new CustomRendererBlockItem(this, new Item.Properties().tab(ModItemGroups.TAB_EASY_VILLAGERS)) {
            @OnlyIn(Dist.CLIENT)
            @Override
            public ItemRenderer createItemRenderer() {
                return new AutoTraderItemRenderer();
            }
        }.setRegistryName(getRegistryName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockGetter, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, blockGetter, components, tooltipFlag);
        AutoTraderTileentity trader = ItemTileEntityCache.getTileEntity(stack, () -> new AutoTraderTileentity(BlockPos.ZERO, ModBlocks.TRADER.defaultBlockState()));
        EasyVillagerEntity villager = trader.getVillagerEntity();
        if (villager != null) {
            components.add(villager.getAdvancedName());
        }
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
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AutoTraderTileentity(blockPos, blockState);
    }
}