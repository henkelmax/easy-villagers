package de.maxhenkel.easyvillagers.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ModBlocks {

    public static final TraderBlock TRADER = new TraderBlock();

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                TRADER
        );

        if (FMLEnvironment.dist == Dist.CLIENT) {
            RenderTypeLookup.setRenderLayer(TRADER, RenderType.getCutout());
        }
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                TRADER.toItem()
        );
    }

}
