package de.maxhenkel.easyvillagers;

import de.maxhenkel.corelib.CommonRegistry;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.ModTileEntities;
import de.maxhenkel.easyvillagers.events.BlockEvents;
import de.maxhenkel.easyvillagers.events.GuiEvents;
import de.maxhenkel.easyvillagers.events.ModSoundEvents;
import de.maxhenkel.easyvillagers.events.VillagerEvents;
import de.maxhenkel.easyvillagers.gui.Containers;
import de.maxhenkel.easyvillagers.integration.IMC;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.loottable.ModLootTables;
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import de.maxhenkel.easyvillagers.net.MessageVillagerParticles;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "easy_villagers";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static ServerConfig SERVER_CONFIG;
    public static ClientConfig CLIENT_CONFIG;

    public static KeyMapping PICKUP_KEY;
    public static KeyMapping CYCLE_TRADES_KEY;

    public Main(IEventBus eventBus) {
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::onRegisterPayloadHandler);
        eventBus.addListener(IMC::enqueueIMC);
        eventBus.addListener(ModTileEntities::onRegisterCapabilities);

        ModBlocks.init(eventBus);
        ModItems.init(eventBus);
        ModTileEntities.init(eventBus);
        Containers.init(eventBus);
        ModCreativeTabs.init(eventBus);
        ModLootTables.init(eventBus);

        SERVER_CONFIG = CommonRegistry.registerConfig(MODID, ModConfig.Type.SERVER, ServerConfig.class);
        CLIENT_CONFIG = CommonRegistry.registerConfig(MODID, ModConfig.Type.CLIENT, ClientConfig.class);

        if (FMLEnvironment.dist.isClient()) {
            eventBus.addListener(Main.this::clientSetup);
            eventBus.addListener(Main.this::onRegisterKeyBinds);
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new VillagerEvents());
        NeoForge.EVENT_BUS.register(new BlockEvents());
    }

    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        ModTileEntities.clientSetup();
        Containers.clientSetup();

        NeoForge.EVENT_BUS.register(new ModSoundEvents());
        NeoForge.EVENT_BUS.register(new GuiEvents());
    }

    public void onRegisterPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("0");
        CommonRegistry.registerMessage(registrar, MessageVillagerParticles.class);
        CommonRegistry.registerMessage(registrar, MessagePickUpVillager.class);
        CommonRegistry.registerMessage(registrar, MessageSelectTrade.class);
        CommonRegistry.registerMessage(registrar, MessageCycleTrades.class);
    }

    @OnlyIn(Dist.CLIENT)
    public void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        PICKUP_KEY = new KeyMapping("key.easy_villagers.pick_up", GLFW.GLFW_KEY_V, "category.easy_villagers");
        CYCLE_TRADES_KEY = new KeyMapping("key.easy_villagers.cycle_trades", GLFW.GLFW_KEY_C, "category.easy_villagers");
        event.register(PICKUP_KEY);
        event.register(CYCLE_TRADES_KEY);
    }

}
