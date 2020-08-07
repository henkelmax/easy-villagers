package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.text.IFormattableTextComponent;
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

        IFormattableTextComponent villager = PluginEasyVillagers.getVillager(tileEntity.getVillagerEntity());
        if (villager != null) {
            tooltip.add(villager);
        }
    }

}