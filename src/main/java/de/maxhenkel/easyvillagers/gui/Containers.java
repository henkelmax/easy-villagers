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

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.<BreederContainer, BreederScreen>registerScreen(BREEDER_CONTAINER, BreederScreen::new);
    }

    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        BREEDER_CONTAINER = new ContainerType<>(BreederContainer::new);
        BREEDER_CONTAINER.setRegistryName(new ResourceLocation(Main.MODID, "breeder"));
        event.getRegistry().register(BREEDER_CONTAINER);
    }

}
