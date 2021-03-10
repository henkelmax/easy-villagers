package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class HUDHandlerVillager implements IComponentProvider {

    static final HUDHandlerVillager INSTANCE = new HUDHandlerVillager();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!(accessor.getTileEntity() instanceof VillagerTileentity)) {
            return;
        }
        VillagerTileentity tileEntity = (VillagerTileentity) accessor.getTileEntity();

        EasyVillagerEntity villager = tileEntity.getVillagerEntity();
        if (villager != null) {
            tooltip.add(villager.getAdvancedName());
        }
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        TileEntity te = accessor.getTileEntity();
        ItemStack stack = new ItemStack(te.getBlockState().getBlock().asItem());
        CompoundNBT blockEntityTag = stack.getOrCreateTagElement("BlockEntityTag");
        te.save(blockEntityTag);
        return stack;
    }
}