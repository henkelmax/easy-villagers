package de.maxhenkel.easyvillagers;

import de.maxhenkel.corelib.CommonRegistry;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import de.maxhenkel.easyvillagers.blocks.tileentity.ModTileEntities;
import de.maxhenkel.easyvillagers.events.BlockEvents;
import de.maxhenkel.easyvillagers.events.VillagerEvents;
import de.maxhenkel.easyvillagers.gui.Containers;
import de.maxhenkel.easyvillagers.items.ModItems;
import de.maxhenkel.easyvillagers.loottable.ModLootTables;
import de.maxhenkel.easyvillagers.net.MessageCycleTrades;
import de.maxhenkel.easyvillagers.net.MessagePickUpVillager;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import de.maxhenkel.easyvillagers.net.MessageVillagerParticles;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

@Mod(EasyVillagersMod.MODID)
@EventBusSubscriber(modid = EasyVillagersMod.MODID)
public class EasyVillagersMod {

    public static final String MODID = "easy_villagers";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static ServerConfig SERVER_CONFIG;
    public static ClientConfig CLIENT_CONFIG;

    public EasyVillagersMod(IEventBus eventBus) {
        eventBus.addListener(ModTileEntities::onRegisterCapabilities);

        ModBlocks.init(eventBus);
        ModItems.init(eventBus);
        ModTileEntities.init(eventBus);
        Containers.init(eventBus);
        ModCreativeTabs.init(eventBus);
        ModLootTables.init(eventBus);

        SERVER_CONFIG = CommonRegistry.registerConfig(MODID, ModConfig.Type.SERVER, ServerConfig.class, true);
        CLIENT_CONFIG = CommonRegistry.registerConfig(MODID, ModConfig.Type.CLIENT, ClientConfig.class);
    }

    @SubscribeEvent
    static void commonSetup(FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new VillagerEvents());
        NeoForge.EVENT_BUS.register(new BlockEvents());
    }


    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (!(itemStack.getItem() instanceof BlockItem blockItem)) {
            return;
        }
        if (!(blockItem.getBlock() instanceof VillagerBlockBase villagerBlock)) {
            return;
        }
        List<Component> components = new LinkedList<>();
        villagerBlock.onTooltip(event.getItemStack(), event.getContext(), components::add);
        for (int i = components.size() - 1; i >= 0; i--) {
            event.getToolTip().add(1, components.get(i));
        }
    }

    @SubscribeEvent
    static void onRegisterPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("0");
        CommonRegistry.registerMessage(registrar, MessageVillagerParticles.class);
        CommonRegistry.registerMessage(registrar, MessagePickUpVillager.class);
        CommonRegistry.registerMessage(registrar, MessageSelectTrade.class);
        CommonRegistry.registerMessage(registrar, MessageCycleTrades.class);
    }

}
