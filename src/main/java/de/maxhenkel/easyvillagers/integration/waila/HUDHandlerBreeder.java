package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class HUDHandlerBreeder implements IComponentProvider {

    static final HUDHandlerBreeder INSTANCE = new HUDHandlerBreeder();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!(accessor.getTileEntity() instanceof BreederTileentity)) {
            return;
        }
        BreederTileentity tileEntity = (BreederTileentity) accessor.getTileEntity();

        IFormattableTextComponent villager1 = PluginEasyVillagers.getVillager(tileEntity.getVillagerEntity1());
        if (villager1 != null) {
            tooltip.add(villager1);
        }

        IFormattableTextComponent villager2 = PluginEasyVillagers.getVillager(tileEntity.getVillagerEntity2());
        if (villager2 != null) {
            tooltip.add(villager2);
        }
    }

}