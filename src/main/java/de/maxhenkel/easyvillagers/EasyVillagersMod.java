package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.blocks.tileentity.ModTileEntities;
import de.maxhenkel.easyvillagers.config.ModConfig;

import de.maxhenkel.easyvillagers.events.VillagerEvents;
import de.maxhenkel.easyvillagers.gui.Containers;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.loottable.ModLootTables;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.Villager;

public class EasyVillagersMod implements ModInitializer {

    public static final String MODID = "easy_villagers";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

        ModBlocks.init();
        ModItems.init();
        ModTileEntities.init();
        Containers.init();
        ModCreativeTabs.init();
        ModLootTables.init();

        de.maxhenkel.easyvillagers.events.BlockEvents.init();

        net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (blockEntity instanceof de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity trader) {
                if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
                    Object config = ((de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) trader).getSlotConfiguration();
                    if (config instanceof de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration trConfig) { if (!de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper.hasConfiguredSide(trConfig, trader.getInputInventory().getContainerSize() + trader.getOutputInventory().getContainerSize(), direction)) return null; return new de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper(trader.getInputInventory(), trader.getOutputInventory(), trConfig, direction); }
                }
                if (direction == net.minecraft.core.Direction.DOWN) {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(trader.getOutputInventory(), direction)) { @Override protected boolean canInsert(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                } else {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(trader.getInputInventory(), direction)) { @Override protected boolean canExtract(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                }
            }
            return null;
        }, ModTileEntities.AUTO_TRADER);

        net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (blockEntity instanceof de.maxhenkel.easyvillagers.blocks.tileentity.BreederTileentity breeder) {
                if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
                    Object config = ((de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) breeder).getSlotConfiguration();
                    if (config instanceof de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration trConfig) { if (!de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper.hasConfiguredSide(trConfig, breeder.getFoodInventory().getContainerSize() + breeder.getOutputInventory().getContainerSize(), direction)) return null; return new de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper(breeder.getFoodInventory(), breeder.getOutputInventory(), trConfig, direction); }
                }
                if (direction == net.minecraft.core.Direction.DOWN) {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(breeder.getOutputInventory(), direction)) { @Override protected boolean canInsert(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                } else {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(breeder.getFoodInventory(), direction)) { @Override protected boolean canExtract(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                }
            }
            return null;
        }, ModTileEntities.BREEDER);

        net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (blockEntity instanceof de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity converter) {
                if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
                    Object config = ((de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) converter).getSlotConfiguration();
                    if (config instanceof de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration trConfig) { if (!de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper.hasConfiguredSide(trConfig, converter.getInputInventory().getContainerSize() + converter.getOutputInventory().getContainerSize(), direction)) return null; return new de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper(converter.getInputInventory(), converter.getOutputInventory(), trConfig, direction); }
                }
                if (direction == net.minecraft.core.Direction.DOWN) {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(converter.getOutputInventory(), direction)) { @Override protected boolean canInsert(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                } else {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(converter.getInputInventory(), direction)) { @Override protected boolean canExtract(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                }
            }
            return null;
        }, ModTileEntities.CONVERTER);

        net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (blockEntity instanceof de.maxhenkel.easyvillagers.blocks.tileentity.IncubatorTileentity incubator) {
                if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
                    Object config = ((de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) incubator).getSlotConfiguration();
                    if (config instanceof de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration trConfig) { if (!de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper.hasConfiguredSide(trConfig, incubator.getInputInventory().getContainerSize() + incubator.getOutputInventory().getContainerSize(), direction)) return null; return new de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper(incubator.getInputInventory(), incubator.getOutputInventory(), trConfig, direction); }
                }
                if (direction == net.minecraft.core.Direction.DOWN) {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(incubator.getOutputInventory(), direction)) { @Override protected boolean canInsert(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                } else {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(incubator.getInputInventory(), direction)) { @Override protected boolean canExtract(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                }
            }
            return null;
        }, ModTileEntities.INCUBATOR);

        net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (blockEntity instanceof de.maxhenkel.easyvillagers.blocks.tileentity.FarmerTileentity farmer) {
                if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
                    Object config = ((de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) farmer).getSlotConfiguration();
                    if (config instanceof de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration trConfig) { if (!de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper.hasConfiguredSide(trConfig, farmer.getOutputInventory().getContainerSize(), direction)) return null; return new de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper(null, farmer.getOutputInventory(), trConfig, direction); }
                }
                if (direction == net.minecraft.core.Direction.DOWN) {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(farmer.getOutputInventory(), direction)) { @Override protected boolean canInsert(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                }
                return null;
            }
            return null;
        }, ModTileEntities.FARMER);

        net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (blockEntity instanceof de.maxhenkel.easyvillagers.blocks.tileentity.IronFarmTileentity ironFarm) {
                if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
                    Object config = ((de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) ironFarm).getSlotConfiguration();
                    if (config instanceof de.maxhenkel.easyvillagers.integration.techreborn.TRSlotConfiguration trConfig) { if (!de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper.hasConfiguredSide(trConfig, ironFarm.getOutputInventory().getContainerSize(), direction)) return null; return new de.maxhenkel.easyvillagers.integration.techreborn.TRStorageWrapper(null, ironFarm.getOutputInventory(), trConfig, direction); }
                }
                if (direction == net.minecraft.core.Direction.DOWN) {
                    return new net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage<>(net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(ironFarm.getOutputInventory(), direction)) { @Override protected boolean canInsert(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant resource) { return false; } };
                }
                return null;
            }
            return null;
        }, ModTileEntities.IRON_FARM);

        de.maxhenkel.easyvillagers.net.Networking.init();

    }

}
