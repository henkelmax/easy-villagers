package de.maxhenkel.easyvillagers.integration.theoneprobe;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import mcjty.theoneprobe.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileInfoProvider implements IProbeInfoProvider {

    public static final ResourceLocation ID = new ResourceLocation(Main.MODID, "probeinfoprovider");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player playerEntity, Level world, BlockState blockState, IProbeHitData iProbeHitData) {
        BlockEntity te = world.getBlockEntity(iProbeHitData.getPos());

        if (te instanceof VillagerTileentity v) {
            addVillager(v.getVillagerEntity(), iProbeInfo);
        } else if (te instanceof BreederTileentity breeder) {
            if (probeMode.equals(ProbeMode.EXTENDED)) {
                addVillager(breeder.getVillagerEntity1(), iProbeInfo);
                addVillager(breeder.getVillagerEntity2(), iProbeInfo);
            }
        }

        if (te instanceof ConverterTileentity converter) {
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
