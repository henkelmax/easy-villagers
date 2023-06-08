package de.maxhenkel.easyvillagers;

import de.maxhenkel.corelib.CommonRegistry;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.ModTileEntities;
import de.maxhenkel.easyvillagers.events.*;
import de.maxhenkel.easyvillagers.gui.Containers;
import de.maxhenkel.easyvillagers.integration.IMC;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import de.maxhenkel.easyvillagers.net.MessageVillagerParticles;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
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

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Main.this::clientSetup);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Main.this::onRegisterKeyBinds);
        });
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new VillagerEvents());
        MinecraftForge.EVENT_BUS.register(new BlockEvents());

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

        MinecraftForge.EVENT_BUS.register(new ModSoundEvents());
        MinecraftForge.EVENT_BUS.register(new GuiEvents());
    }

    @OnlyIn(Dist.CLIENT)
    public void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        PICKUP_KEY = new KeyMapping("key.easy_villagers.pick_up", GLFW.GLFW_KEY_V, "category.easy_villagers");
        CYCLE_TRADES_KEY = new KeyMapping("key.easy_villagers.cycle_trades", GLFW.GLFW_KEY_C, "category.easy_villagers");
        event.register(PICKUP_KEY);
        event.register(CYCLE_TRADES_KEY);
    }

}
