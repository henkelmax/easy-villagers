package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

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

}
