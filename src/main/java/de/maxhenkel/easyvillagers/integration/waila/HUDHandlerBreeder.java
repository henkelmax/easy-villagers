package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;

public class HUDHandlerBreeder implements IComponentProvider {

    public static final HUDHandlerBreeder INSTANCE = new HUDHandlerBreeder();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof BreederTileentity breeder) {
            EasyVillagerEntity villager1 = breeder.getVillagerEntity1();
            if (villager1 != null) {
                iTooltip.add(villager1.getAdvancedName());
            }

            EasyVillagerEntity villager2 = breeder.getVillagerEntity2();
            if (villager2 != null) {
                iTooltip.add(villager2.getAdvancedName());
            }
        }
    }
}