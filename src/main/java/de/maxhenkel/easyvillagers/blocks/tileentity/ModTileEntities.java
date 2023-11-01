package de.maxhenkel.easyvillagers.blocks.tileentity;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.blocks.tileentity.render.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class ModTileEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);

    public static final RegistryObject<BlockEntityType<TraderTileentity>> TRADER = BLOCK_ENTITY_REGISTER.register("trader", () ->
            BlockEntityType.Builder.of(TraderTileentity::new, ModBlocks.TRADER.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<AutoTraderTileentity>> AUTO_TRADER = BLOCK_ENTITY_REGISTER.register("auto_trader", () ->
            BlockEntityType.Builder.of(AutoTraderTileentity::new, ModBlocks.AUTO_TRADER.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<FarmerTileentity>> FARMER = BLOCK_ENTITY_REGISTER.register("farmer", () ->
            BlockEntityType.Builder.of(FarmerTileentity::new, ModBlocks.FARMER.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<BreederTileentity>> BREEDER = BLOCK_ENTITY_REGISTER.register("breeder", () ->
            BlockEntityType.Builder.of(BreederTileentity::new, ModBlocks.BREEDER.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<ConverterTileentity>> CONVERTER = BLOCK_ENTITY_REGISTER.register("converter", () ->
            BlockEntityType.Builder.of(ConverterTileentity::new, ModBlocks.CONVERTER.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<IronFarmTileentity>> IRON_FARM = BLOCK_ENTITY_REGISTER.register("iron_farm", () ->
            BlockEntityType.Builder.of(IronFarmTileentity::new, ModBlocks.IRON_FARM.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<IncubatorTileentity>> INCUBATOR = BLOCK_ENTITY_REGISTER.register("incubator", () ->
            BlockEntityType.Builder.of(IncubatorTileentity::new, ModBlocks.INCUBATOR.get()).build(null)
    );

    public static void init() {
        BLOCK_ENTITY_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        if (!Main.CLIENT_CONFIG.renderBlockContents.get()) {
            return;
        }
        BlockEntityRenderers.register(ModTileEntities.TRADER.get(), TraderRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.AUTO_TRADER.get(), AutoTraderRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.FARMER.get(), FarmerRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.BREEDER.get(), BreederRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.CONVERTER.get(), ConverterRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.IRON_FARM.get(), IronFarmRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.INCUBATOR.get(), IncubatorRenderer::new);
    }

}
