package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.BreederRenderer;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.FarmerRenderer;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.TraderRenderer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModTileEntities {

    public static TileEntityType<TraderTileentity> TRADER;
    public static TileEntityType<FarmerTileentity> FARMER;
    public static TileEntityType<BreederTileentity> BREEDER;

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        TRADER = TileEntityType.Builder.create(TraderTileentity::new, ModBlocks.TRADER).build(null);
        TRADER.setRegistryName(new ResourceLocation(Main.MODID, "trader"));
        event.getRegistry().register(TRADER);

        FARMER = TileEntityType.Builder.create(FarmerTileentity::new, ModBlocks.FARMER).build(null);
        FARMER.setRegistryName(new ResourceLocation(Main.MODID, "farmer"));
        event.getRegistry().register(FARMER);

        BREEDER = TileEntityType.Builder.create(BreederTileentity::new, ModBlocks.BREEDER).build(null);
        BREEDER.setRegistryName(new ResourceLocation(Main.MODID, "breeder"));
        event.getRegistry().register(BREEDER);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TRADER, TraderRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.FARMER, FarmerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.BREEDER, BreederRenderer::new);
    }

}
