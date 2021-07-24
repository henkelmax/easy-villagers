package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

public class ModTileEntities {

    public static BlockEntityType<TraderTileentity> TRADER;
    public static BlockEntityType<AutoTraderTileentity> AUTO_TRADER;
    public static BlockEntityType<FarmerTileentity> FARMER;
    public static BlockEntityType<BreederTileentity> BREEDER;
    public static BlockEntityType<ConverterTileentity> CONVERTER;
    public static BlockEntityType<IronFarmTileentity> IRON_FARM;
    public static BlockEntityType<IncubatorTileentity> INCUBATOR;

    public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        TRADER = BlockEntityType.Builder.of(TraderTileentity::new, ModBlocks.TRADER).build(null);
        TRADER.setRegistryName(new ResourceLocation(Main.MODID, "trader"));
        event.getRegistry().register(TRADER);

        AUTO_TRADER = BlockEntityType.Builder.of(AutoTraderTileentity::new, ModBlocks.AUTO_TRADER).build(null);
        AUTO_TRADER.setRegistryName(new ResourceLocation(Main.MODID, "auto_trader"));
        event.getRegistry().register(AUTO_TRADER);

        FARMER = BlockEntityType.Builder.of(FarmerTileentity::new, ModBlocks.FARMER).build(null);
        FARMER.setRegistryName(new ResourceLocation(Main.MODID, "farmer"));
        event.getRegistry().register(FARMER);

        BREEDER = BlockEntityType.Builder.of(BreederTileentity::new, ModBlocks.BREEDER).build(null);
        BREEDER.setRegistryName(new ResourceLocation(Main.MODID, "breeder"));
        event.getRegistry().register(BREEDER);

        CONVERTER = BlockEntityType.Builder.of(ConverterTileentity::new, ModBlocks.CONVERTER).build(null);
        CONVERTER.setRegistryName(new ResourceLocation(Main.MODID, "converter"));
        event.getRegistry().register(CONVERTER);

        IRON_FARM = BlockEntityType.Builder.of(IronFarmTileentity::new, ModBlocks.IRON_FARM).build(null);
        IRON_FARM.setRegistryName(new ResourceLocation(Main.MODID, "iron_farm"));
        event.getRegistry().register(IRON_FARM);

        INCUBATOR = BlockEntityType.Builder.of(IncubatorTileentity::new, ModBlocks.INCUBATOR).build(null);
        INCUBATOR.setRegistryName(new ResourceLocation(Main.MODID, "incubator"));
        event.getRegistry().register(INCUBATOR);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        BlockEntityRenderers.register(ModTileEntities.TRADER, TraderRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.AUTO_TRADER, AutoTraderRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.FARMER, FarmerRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.BREEDER, BreederRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.CONVERTER, ConverterRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.IRON_FARM, IronFarmRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.INCUBATOR, IncubatorRenderer::new);
    }

}
