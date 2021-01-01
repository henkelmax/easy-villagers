package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModTileEntities {

    public static TileEntityType<TraderTileentity> TRADER;
    public static TileEntityType<AutoTraderTileentity> AUTO_TRADER;
    public static TileEntityType<FarmerTileentity> FARMER;
    public static TileEntityType<BreederTileentity> BREEDER;
    public static TileEntityType<ConverterTileentity> CONVERTER;
    public static TileEntityType<IronFarmTileentity> IRON_FARM;
    public static TileEntityType<IncubatorTileentity> INCUBATOR;

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        TRADER = TileEntityType.Builder.create(TraderTileentity::new, ModBlocks.TRADER).build(null);
        TRADER.setRegistryName(new ResourceLocation(Main.MODID, "trader"));
        event.getRegistry().register(TRADER);

        AUTO_TRADER = TileEntityType.Builder.create(AutoTraderTileentity::new, ModBlocks.AUTO_TRADER).build(null);
        AUTO_TRADER.setRegistryName(new ResourceLocation(Main.MODID, "auto_trader"));
        event.getRegistry().register(AUTO_TRADER);

        FARMER = TileEntityType.Builder.create(FarmerTileentity::new, ModBlocks.FARMER).build(null);
        FARMER.setRegistryName(new ResourceLocation(Main.MODID, "farmer"));
        event.getRegistry().register(FARMER);

        BREEDER = TileEntityType.Builder.create(BreederTileentity::new, ModBlocks.BREEDER).build(null);
        BREEDER.setRegistryName(new ResourceLocation(Main.MODID, "breeder"));
        event.getRegistry().register(BREEDER);

        CONVERTER = TileEntityType.Builder.create(ConverterTileentity::new, ModBlocks.CONVERTER).build(null);
        CONVERTER.setRegistryName(new ResourceLocation(Main.MODID, "converter"));
        event.getRegistry().register(CONVERTER);

        IRON_FARM = TileEntityType.Builder.create(IronFarmTileentity::new, ModBlocks.IRON_FARM).build(null);
        IRON_FARM.setRegistryName(new ResourceLocation(Main.MODID, "iron_farm"));
        event.getRegistry().register(IRON_FARM);

        INCUBATOR = TileEntityType.Builder.create(IncubatorTileentity::new, ModBlocks.INCUBATOR).build(null);
        INCUBATOR.setRegistryName(new ResourceLocation(Main.MODID, "incubator"));
        event.getRegistry().register(INCUBATOR);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TRADER, TraderRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.AUTO_TRADER, AutoTraderRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.FARMER, FarmerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.BREEDER, BreederRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CONVERTER, ConverterRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.IRON_FARM, IronFarmRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.INCUBATOR, IncubatorRenderer::new);
    }

}
