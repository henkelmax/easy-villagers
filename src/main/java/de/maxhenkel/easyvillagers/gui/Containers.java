package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class Containers {

    public static final MenuType<AutoTraderContainer> AUTO_TRADER_CONTAINER = registerMenu("auto_trader", new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>((id, inv, data) -> new AutoTraderContainer(id, inv, data), net.minecraft.core.BlockPos.STREAM_CODEC));
    public static final MenuType<BreederContainer> BREEDER_CONTAINER = registerMenu("breeder", new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>((id, inv, data) -> new BreederContainer(id, inv, data), net.minecraft.core.BlockPos.STREAM_CODEC));
    public static final MenuType<ConverterContainer> CONVERTER_CONTAINER = registerMenu("converter", new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>((id, inv, data) -> new ConverterContainer(id, inv, data), net.minecraft.core.BlockPos.STREAM_CODEC));
    public static final MenuType<IncubatorContainer> INCUBATOR_CONTAINER = registerMenu("incubator", new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>((id, inv, data) -> new IncubatorContainer(id, inv, data), net.minecraft.core.BlockPos.STREAM_CODEC));
    public static final MenuType<OutputContainer> OUTPUT_CONTAINER = registerMenu("output", new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>((id, inv, data) -> new OutputContainer(id, inv, data), net.minecraft.core.BlockPos.STREAM_CODEC));
    // Notice: InventoryViewerContainer has blockPos in the original NeoForge mod. We may need to use Fabric Extended Screen Handler here later, but for now we register a generic MenuType.
    public static final MenuType<InventoryViewerContainer> INVENTORY_VIEWER_CONTAINER = registerMenu("inventory_viewer", new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>((id, inv, data) -> new InventoryViewerContainer(id, inv, data), net.minecraft.core.BlockPos.STREAM_CODEC));

    private static <T extends MenuType<?>> T registerMenu(String name, T menuType) {
        return Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, name), menuType);
    }

    public static void init() {
    }

}
