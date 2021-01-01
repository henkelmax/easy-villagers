package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.ModItemGroups;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.items.render.TraderItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class TraderBlock extends TraderBlockBase {

    public TraderBlock() {
        setRegistryName(new ResourceLocation(Main.MODID, "trader"));
    }

    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().group(ModItemGroups.TAB_EASY_VILLAGERS).setISTER(() -> TraderItemRenderer::new)).setRegistryName(getRegistryName());
    }

    @Override
    protected boolean openGUI(TraderTileentityBase trader, PlayerEntity player) {
        return trader.openTradingGUI(player);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TraderTileentity();
    }

}
