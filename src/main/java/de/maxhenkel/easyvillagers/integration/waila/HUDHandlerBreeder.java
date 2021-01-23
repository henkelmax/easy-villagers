package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
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

        EasyVillagerEntity villager1 = tileEntity.getVillagerEntity1();
        if (villager1 != null) {
            tooltip.add(villager1.getAdvancedName());
        }

        EasyVillagerEntity villager2 = tileEntity.getVillagerEntity2();
        if (villager2 != null) {
            tooltip.add(villager2.getAdvancedName());
        }
    }

}