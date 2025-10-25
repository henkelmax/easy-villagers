package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    private static final DeferredRegister.Blocks BLOCK_REGISTER = DeferredRegister.createBlocks(EasyVillagersMod.MODID);

    public static final DeferredHolder<Block, TraderBlock> TRADER = BLOCK_REGISTER.registerBlock("trader", TraderBlock::new, BlockBehaviour.Properties::of);
    public static final DeferredHolder<Block, AutoTraderBlock> AUTO_TRADER = BLOCK_REGISTER.registerBlock("auto_trader", AutoTraderBlock::new, BlockBehaviour.Properties::of);
    public static final DeferredHolder<Block, FarmerBlock> FARMER = BLOCK_REGISTER.registerBlock("farmer", FarmerBlock::new, BlockBehaviour.Properties::of);
    public static final DeferredHolder<Block, BreederBlock> BREEDER = BLOCK_REGISTER.registerBlock("breeder", BreederBlock::new, BlockBehaviour.Properties::of);
    public static final DeferredHolder<Block, ConverterBlock> CONVERTER = BLOCK_REGISTER.registerBlock("converter", ConverterBlock::new, BlockBehaviour.Properties::of);
    public static final DeferredHolder<Block, IronFarmBlock> IRON_FARM = BLOCK_REGISTER.registerBlock("iron_farm", IronFarmBlock::new, BlockBehaviour.Properties::of);
    public static final DeferredHolder<Block, IncubatorBlock> INCUBATOR = BLOCK_REGISTER.registerBlock("incubator", IncubatorBlock::new, BlockBehaviour.Properties::of);
    public static final DeferredHolder<Block, InventoryViewerBlock> INVENTORY_VIEWER = BLOCK_REGISTER.registerBlock("inventory_viewer", InventoryViewerBlock::new, BlockBehaviour.Properties::of);

    public static void init(IEventBus eventBus) {
        BLOCK_REGISTER.register(eventBus);
    }

}
