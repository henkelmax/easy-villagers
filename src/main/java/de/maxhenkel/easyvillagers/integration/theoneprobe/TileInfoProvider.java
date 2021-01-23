package de.maxhenkel.easyvillagers.integration.theoneprobe;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return Main.MODID + ":probeinfoprovider";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData) {
        TileEntity te = world.getTileEntity(iProbeHitData.getPos());

        if (te instanceof VillagerTileentity) {
            VillagerTileentity villagerTileentity = (VillagerTileentity) te;
            addVillager(villagerTileentity.getVillagerEntity(), iProbeInfo);
        } else if (te instanceof BreederTileentity) {
            if (probeMode.equals(ProbeMode.EXTENDED)) {
                BreederTileentity breederTileentity = (BreederTileentity) te;
                addVillager(breederTileentity.getVillagerEntity1(), iProbeInfo);
                addVillager(breederTileentity.getVillagerEntity2(), iProbeInfo);
            }
        }

        if (te instanceof ConverterTileentity) {
            ConverterTileentity converter = (ConverterTileentity) te;
            long timer = converter.getTimer();
            if (timer >= 0 && converter.hasVillager()) {
                iProbeInfo.progress(timer, ConverterTileentity.getConvertTime(), iProbeInfo.defaultProgressStyle().showText(false));
            }
        }
    }

    private void addVillager(EasyVillagerEntity villager, IProbeInfo iProbeInfo) {
        if (villager != null) {
            IProbeInfo info = iProbeInfo.horizontal(iProbeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
            info.entity(villager).text(villager.getAdvancedName());
        }
    }
}
