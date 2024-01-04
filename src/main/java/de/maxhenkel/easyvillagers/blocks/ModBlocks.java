package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, Main.MODID);

    public static final DeferredHolder<Block, TraderBlock> TRADER = BLOCK_REGISTER.register("trader", TraderBlock::new);
    public static final DeferredHolder<Block, AutoTraderBlock> AUTO_TRADER = BLOCK_REGISTER.register("auto_trader", AutoTraderBlock::new);
    public static final DeferredHolder<Block, FarmerBlock> FARMER = BLOCK_REGISTER.register("farmer", FarmerBlock::new);
    public static final DeferredHolder<Block, BreederBlock> BREEDER = BLOCK_REGISTER.register("breeder", BreederBlock::new);
    public static final DeferredHolder<Block, ConverterBlock> CONVERTER = BLOCK_REGISTER.register("converter", ConverterBlock::new);
    public static final DeferredHolder<Block, IronFarmBlock> IRON_FARM = BLOCK_REGISTER.register("iron_farm", IronFarmBlock::new);
    public static final DeferredHolder<Block, IncubatorBlock> INCUBATOR = BLOCK_REGISTER.register("incubator", IncubatorBlock::new);
    public static final DeferredHolder<Block, InventoryViewerBlock> INVENTORY_VIEWER = BLOCK_REGISTER.register("inventory_viewer", InventoryViewerBlock::new);

    public static void init(IEventBus eventBus) {
        BLOCK_REGISTER.register(eventBus);
    }

}
