package de.maxhenkel.easyvillagers;

import de.maxhenkel.easyvillagers.blocks.tileentity.ModClientTileEntities;
import de.maxhenkel.easyvillagers.events.GuiEvents;
import de.maxhenkel.easyvillagers.events.ModSoundEvents;
import de.maxhenkel.easyvillagers.gui.Containers;
import de.maxhenkel.easyvillagers.items.render.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

@Mod(value = EasyVillagersMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = EasyVillagersMod.MODID, value = Dist.CLIENT)
public class EasyVillagersClientMod {

    public static KeyMapping CYCLE_TRADES_KEY;
    public static KeyMapping PICKUP_KEY;

    public EasyVillagersClientMod(IEventBus eventBus) {
        Containers.initClient(eventBus);
    }

    @SubscribeEvent
    static void clientSetup(FMLClientSetupEvent event) {
        ModClientTileEntities.clientSetup();

        NeoForge.EVENT_BUS.register(new ModSoundEvents());
        NeoForge.EVENT_BUS.register(new GuiEvents());
    }

    @SubscribeEvent
    static void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        PICKUP_KEY = new KeyMapping("key.easy_villagers.pick_up", GLFW.GLFW_KEY_V, "category.easy_villagers");
        CYCLE_TRADES_KEY = new KeyMapping("key.easy_villagers.cycle_trades", GLFW.GLFW_KEY_C, "category.easy_villagers");
        event.register(PICKUP_KEY);
        event.register(CYCLE_TRADES_KEY);
    }

    @SubscribeEvent
    static void registerItemModels(RegisterSpecialModelRendererEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "auto_trader"), AutoTraderSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "breeder"), BreederSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "converter"), ConverterSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "farmer"), FarmerSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "incubator"), IncubatorSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "inventory_viewer"), InventoryViewerSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "trader"), TraderSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "iron_farm"), IronFarmSpecialRenderer.Unbaked.MAP_CODEC);

        event.register(ResourceLocation.fromNamespaceAndPath(EasyVillagersMod.MODID, "villager"), VillagerSpecialRenderer.Unbaked.MAP_CODEC);
    }

}
