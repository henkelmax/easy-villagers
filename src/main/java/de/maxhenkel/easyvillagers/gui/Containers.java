package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.ClientRegistry;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Containers {

    private static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, Main.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<AutoTraderContainer>> AUTO_TRADER_CONTAINER = MENU_TYPE_REGISTER.register("auto_trader", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new AutoTraderContainer(windowId, inv))
    );
    public static final DeferredHolder<MenuType<?>, MenuType<BreederContainer>> BREEDER_CONTAINER = MENU_TYPE_REGISTER.register("breeder", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new BreederContainer(windowId, inv))
    );
    public static final DeferredHolder<MenuType<?>, MenuType<ConverterContainer>> CONVERTER_CONTAINER = MENU_TYPE_REGISTER.register("converter", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new ConverterContainer(windowId, inv))
    );
    public static final DeferredHolder<MenuType<?>, MenuType<IncubatorContainer>> INCUBATOR_CONTAINER = MENU_TYPE_REGISTER.register("incubator", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new IncubatorContainer(windowId, inv))
    );
    public static final DeferredHolder<MenuType<?>, MenuType<OutputContainer>> OUTPUT_CONTAINER = MENU_TYPE_REGISTER.register("output", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new OutputContainer(windowId, inv))
    );
    public static final DeferredHolder<MenuType<?>, MenuType<InventoryViewerContainer>> INVENTORY_VIEWER_CONTAINER = MENU_TYPE_REGISTER.register("inventory_viewer", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new InventoryViewerContainer(windowId, inv, data.readBlockPos()))
    );

    public static void init(IEventBus eventBus) {
        MENU_TYPE_REGISTER.register(eventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.<AutoTraderContainer, AutoTraderScreen>registerScreen(AUTO_TRADER_CONTAINER.get(), AutoTraderScreen::new);
        ClientRegistry.<BreederContainer, BreederScreen>registerScreen(BREEDER_CONTAINER.get(), BreederScreen::new);
        ClientRegistry.<ConverterContainer, ConverterScreen>registerScreen(CONVERTER_CONTAINER.get(), ConverterScreen::new);
        ClientRegistry.<IncubatorContainer, IncubatorScreen>registerScreen(INCUBATOR_CONTAINER.get(), IncubatorScreen::new);
        ClientRegistry.<OutputContainer, OutputScreen>registerScreen(OUTPUT_CONTAINER.get(), OutputScreen::new);
        ClientRegistry.<InventoryViewerContainer, InventoryViewerScreen>registerScreen(INVENTORY_VIEWER_CONTAINER.get(), InventoryViewerScreen::new);
    }

}
