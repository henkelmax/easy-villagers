package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.ClientRegistry;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

public class Containers {

    public static MenuType<AutoTraderContainer> AUTO_TRADER_CONTAINER;
    public static MenuType<BreederContainer> BREEDER_CONTAINER;
    public static MenuType<VillagerIOContainer> VILLAGER_IO_CONTAINER;
    public static MenuType<OutputContainer> OUTPUT_CONTAINER;

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.<AutoTraderContainer, AutoTraderScreen>registerScreen(AUTO_TRADER_CONTAINER, AutoTraderScreen::new);
        ClientRegistry.<BreederContainer, BreederScreen>registerScreen(BREEDER_CONTAINER, BreederScreen::new);
        ClientRegistry.<VillagerIOContainer, VillagerIOScreen>registerScreen(VILLAGER_IO_CONTAINER, VillagerIOScreen::new);
        ClientRegistry.<OutputContainer, OutputScreen>registerScreen(OUTPUT_CONTAINER, OutputScreen::new);
    }

    public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        AUTO_TRADER_CONTAINER = new MenuType<>(AutoTraderContainer::new);
        AUTO_TRADER_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "auto_trader"));
        event.getRegistry().register(AUTO_TRADER_CONTAINER);

        BREEDER_CONTAINER = new MenuType<>(BreederContainer::new);
        BREEDER_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "breeder"));
        event.getRegistry().register(BREEDER_CONTAINER);

        VILLAGER_IO_CONTAINER = new MenuType<>(VillagerIOContainer::new);
        VILLAGER_IO_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "converter"));
        event.getRegistry().register(VILLAGER_IO_CONTAINER);

        OUTPUT_CONTAINER = new MenuType<>(OutputContainer::new);
        OUTPUT_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "output"));
        event.getRegistry().register(OUTPUT_CONTAINER);
    }

}
