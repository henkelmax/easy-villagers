package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.ModItemGroups;
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.AutoTraderRenderer;
import de.maxhenkel.easyvillagers.gui.AutoTraderContainer;
import de.maxhenkel.easyvillagers.items.render.BlockItemRendererBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AutoTraderBlock extends TraderBlockBase {

    public AutoTraderBlock() {
        setRegistryName(new ResourceLocation(Main.MODID, "auto_trader"));
    }

    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().group(ModItemGroups.TAB_EASY_VILLAGERS).setISTER(() -> () -> new BlockItemRendererBase<>(AutoTraderRenderer::new, AutoTraderTileentity::new))).setRegistryName(getRegistryName());
    }

    @Override
    protected boolean openGUI(TraderTileentityBase trader, PlayerEntity player) {
        player.openContainer(new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("block.easy_villagers.auto_trader");
            }

            @Nullable
            @Override
            public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
                AutoTraderTileentity autoTrader = (AutoTraderTileentity) trader;
                return new AutoTraderContainer(id, inv, autoTrader, autoTrader.getInputInventory(), autoTrader.getOutputInventory());
            }
        });
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new AutoTraderTileentity();
    }

}