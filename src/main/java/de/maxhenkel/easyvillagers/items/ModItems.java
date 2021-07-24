package de.maxhenkel.easyvillagers.items;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ModItems {

    public static VillagerItem VILLAGER = new VillagerItem();

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                VILLAGER.setRegistryName(Main.MODID, "villager")
        );
    }

}
