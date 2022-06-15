package de.maxhenkel.easyvillagers.integration.waila;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class HUDHandlerBreeder implements IBlockComponentProvider {

    public static final HUDHandlerBreeder INSTANCE = new HUDHandlerBreeder();

    private static final ResourceLocation UID = new ResourceLocation(Main.MODID, "breeder");

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

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}