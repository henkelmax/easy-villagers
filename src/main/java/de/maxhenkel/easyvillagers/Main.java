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
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import de.maxhenkel.easyvillagers.net.MessageVillagerParticles;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "easy_villagers";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static ServerConfig SERVER_CONFIG;
    public static ClientConfig CLIENT_CONFIG;

    public static SimpleChannel SIMPLE_CHANNEL;
    public static KeyMapping PICKUP_KEY;
    public static KeyMapping CYCLE_TRADES_KEY;

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(IMC::enqueueIMC);

        ModBlocks.init();
        ModItems.init();
        ModTileEntities.init();
        Containers.init();
        ModCreativeTabs.init();

        SERVER_CONFIG = CommonRegistry.registerConfig(ModConfig.Type.SERVER, ServerConfig.class);
        CLIENT_CONFIG = CommonRegistry.registerConfig(ModConfig.Type.CLIENT, ClientConfig.class);

        if (FMLEnvironment.dist.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Main.this::clientSetup);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Main.this::onRegisterKeyBinds);
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new VillagerEvents());
        NeoForge.EVENT_BUS.register(new BlockEvents());

        SIMPLE_CHANNEL = CommonRegistry.registerChannel(Main.MODID, "default");
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 0, MessageVillagerParticles.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 1, MessagePickUpVillager.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 2, MessageSelectTrade.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 3, MessageCycleTrades.class);
    }

    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        ModTileEntities.clientSetup();
        Containers.clientSetup();

        NeoForge.EVENT_BUS.register(new ModSoundEvents());
        NeoForge.EVENT_BUS.register(new GuiEvents());
    }

    @OnlyIn(Dist.CLIENT)
    public void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        PICKUP_KEY = new KeyMapping("key.easy_villagers.pick_up", GLFW.GLFW_KEY_V, "category.easy_villagers");
        CYCLE_TRADES_KEY = new KeyMapping("key.easy_villagers.cycle_trades", GLFW.GLFW_KEY_C, "category.easy_villagers");
        event.register(PICKUP_KEY);
        event.register(CYCLE_TRADES_KEY);
    }

}
