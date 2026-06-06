package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.blocks.tileentity.ModClientTileEntities;

import de.maxhenkel.easyvillagers.gui.*;
import de.maxhenkel.easyvillagers.items.render.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class EasyVillagersClientMod implements ClientModInitializer {

    public static final net.minecraft.client.KeyMapping.Category CATEGORY = net.minecraft.client.KeyMapping.Category.register(net.minecraft.resources.Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "category"));
    public static KeyMapping CYCLE_TRADES_KEY;
    public static KeyMapping PICKUP_KEY;

    @Override
    public void onInitializeClient() {
        ModClientTileEntities.clientSetup();
        de.maxhenkel.easyvillagers.events.VillagerEvents.clientSetup();
        de.maxhenkel.easyvillagers.events.GuiEvents.init();
        
        net.minecraft.client.gui.screens.MenuScreens.register(Containers.AUTO_TRADER_CONTAINER, AutoTraderScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(Containers.BREEDER_CONTAINER, BreederScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(Containers.CONVERTER_CONTAINER, ConverterScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(Containers.INCUBATOR_CONTAINER, IncubatorScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(Containers.OUTPUT_CONTAINER, OutputScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(Containers.INVENTORY_VIEWER_CONTAINER, InventoryViewerScreen::new);
        
        

        PICKUP_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.easy_villagers.pick_up",
                GLFW.GLFW_KEY_V,
                CATEGORY
        ));
        CYCLE_TRADES_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.easy_villagers.cycle_trades",
                GLFW.GLFW_KEY_C,
                CATEGORY
        ));

        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "auto_trader"), AutoTraderSpecialRenderer.Unbaked.MAP_CODEC);
        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "breeder"), BreederSpecialRenderer.Unbaked.MAP_CODEC);
        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "converter"), ConverterSpecialRenderer.Unbaked.MAP_CODEC);
        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "farmer"), FarmerSpecialRenderer.Unbaked.MAP_CODEC);
        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "incubator"), IncubatorSpecialRenderer.Unbaked.MAP_CODEC);
        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "inventory_viewer"), InventoryViewerSpecialRenderer.Unbaked.MAP_CODEC);
        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "iron_farm"), IronFarmSpecialRenderer.Unbaked.MAP_CODEC);
        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "trader"), TraderSpecialRenderer.Unbaked.MAP_CODEC);
        net.minecraft.client.renderer.special.SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "villager"), VillagerSpecialRenderer.Unbaked.MAP_CODEC);
    }

}
