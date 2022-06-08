package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);

    public static final RegistryObject<TraderBlock> TRADER = BLOCK_REGISTER.register("trader", TraderBlock::new);
    public static final RegistryObject<AutoTraderBlock> AUTO_TRADER = BLOCK_REGISTER.register("auto_trader", AutoTraderBlock::new);
    public static final RegistryObject<FarmerBlock> FARMER = BLOCK_REGISTER.register("farmer", FarmerBlock::new);
    public static final RegistryObject<BreederBlock> BREEDER = BLOCK_REGISTER.register("breeder", BreederBlock::new);
    public static final RegistryObject<ConverterBlock> CONVERTER = BLOCK_REGISTER.register("converter", ConverterBlock::new);
    public static final RegistryObject<IronFarmBlock> IRON_FARM = BLOCK_REGISTER.register("iron_farm", IronFarmBlock::new);
    public static final RegistryObject<IncubatorBlock> INCUBATOR = BLOCK_REGISTER.register("incubator", IncubatorBlock::new);

    public static void init() {
        BLOCK_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void clientSetup() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ItemBlockRenderTypes.setRenderLayer(TRADER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(AUTO_TRADER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(FARMER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BREEDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CONVERTER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(IRON_FARM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(INCUBATOR.get(), RenderType.cutout());
        }
    }

}
