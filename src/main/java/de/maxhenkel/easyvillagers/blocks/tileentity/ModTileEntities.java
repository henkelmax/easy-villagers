package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModTileEntities {

    public static TileEntityType<TraderTileentity> TRADER;

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        TRADER = TileEntityType.Builder.create(() -> new TraderTileentity(), ModBlocks.TRADER).build(null);
        TRADER.setRegistryName(new ResourceLocation(Main.MODID, "trader"));
        event.getRegistry().register(TRADER);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TRADER, TraderRenderer::new);
    }

}
