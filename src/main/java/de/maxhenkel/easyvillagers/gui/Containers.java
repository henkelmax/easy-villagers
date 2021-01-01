package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.ClientRegistry;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

public class Containers {

    public static ContainerType<AutoTraderContainer> AUTO_TRADER_CONTAINER;
    public static ContainerType<BreederContainer> BREEDER_CONTAINER;
    public static ContainerType<VillagerIOContainer> VILLAGER_IO_CONTAINER;
    public static ContainerType<OutputContainer> OUTPUT_CONTAINER;

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.<AutoTraderContainer, AutoTraderScreen>registerScreen(AUTO_TRADER_CONTAINER, AutoTraderScreen::new);
        ClientRegistry.<BreederContainer, BreederScreen>registerScreen(BREEDER_CONTAINER, BreederScreen::new);
        ClientRegistry.<VillagerIOContainer, VillagerIOScreen>registerScreen(VILLAGER_IO_CONTAINER, VillagerIOScreen::new);
        ClientRegistry.<OutputContainer, OutputScreen>registerScreen(OUTPUT_CONTAINER, OutputScreen::new);
    }

    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        AUTO_TRADER_CONTAINER = new ContainerType<>(AutoTraderContainer::new);
        AUTO_TRADER_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "auto_trader"));
        event.getRegistry().register(AUTO_TRADER_CONTAINER);

        BREEDER_CONTAINER = new ContainerType<>(BreederContainer::new);
        BREEDER_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "breeder"));
        event.getRegistry().register(BREEDER_CONTAINER);

        VILLAGER_IO_CONTAINER = new ContainerType<>(VillagerIOContainer::new);
        VILLAGER_IO_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "converter"));
        event.getRegistry().register(VILLAGER_IO_CONTAINER);

        OUTPUT_CONTAINER = new ContainerType<>(OutputContainer::new);
        OUTPUT_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "output"));
        event.getRegistry().register(OUTPUT_CONTAINER);
    }

}
