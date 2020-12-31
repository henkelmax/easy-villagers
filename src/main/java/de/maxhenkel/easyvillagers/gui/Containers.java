package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.ClientRegistry;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

public class Containers {

    public static ContainerType<BreederContainer> BREEDER_CONTAINER;
    public static ContainerType<ConverterContainer> CONVERTER_CONTAINER;
    public static ContainerType<OutputContainer> OUTPUT_CONTAINER;
    public static ContainerType<IncubatorContainer> INCUBATOR_CONTAINER;

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.<BreederContainer, BreederScreen>registerScreen(BREEDER_CONTAINER, BreederScreen::new);
        ClientRegistry.<ConverterContainer, ConverterScreen>registerScreen(CONVERTER_CONTAINER, ConverterScreen::new);
        ClientRegistry.<OutputContainer, OutputScreen>registerScreen(OUTPUT_CONTAINER, OutputScreen::new);
        ClientRegistry.<IncubatorContainer, IncubatorScreen>registerScreen(INCUBATOR_CONTAINER, IncubatorScreen::new);
    }

    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        BREEDER_CONTAINER = new ContainerType<>(BreederContainer::new);
        BREEDER_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "breeder"));
        event.getRegistry().register(BREEDER_CONTAINER);

        CONVERTER_CONTAINER = new ContainerType<>(ConverterContainer::new);
        CONVERTER_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "converter"));
        event.getRegistry().register(CONVERTER_CONTAINER);

        OUTPUT_CONTAINER = new ContainerType<>(OutputContainer::new);
        OUTPUT_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "output"));
        event.getRegistry().register(OUTPUT_CONTAINER);

        INCUBATOR_CONTAINER = new ContainerType<>(IncubatorContainer::new);
        INCUBATOR_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "incubator"));
        event.getRegistry().register(INCUBATOR_CONTAINER);
    }

}
