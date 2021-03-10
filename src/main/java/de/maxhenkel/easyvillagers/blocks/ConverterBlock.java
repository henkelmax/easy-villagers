package de.maxhenkel.easyvillagers.blocks;

import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.ModItemGroups;
import de.maxhenkel.easyvillagers.blocks.tileentity.ConverterTileentity;
import de.maxhenkel.easyvillagers.gui.VillagerIOContainer;
import de.maxhenkel.easyvillagers.items.render.ConverterItemRenderer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ConverterBlock extends VillagerBlockBase implements ITileEntityProvider, IItemBlock {

    public ConverterBlock() {
        super(Properties.of(Material.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion());
        setRegistryName(new ResourceLocation(Main.MODID, "converter"));
    }

    @Override
    public Item toItem() {
        return new BlockItem(this, new Item.Properties().tab(ModItemGroups.TAB_EASY_VILLAGERS).setISTER(() -> ConverterItemRenderer::new)).setRegistryName(getRegistryName());
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof ConverterTileentity)) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
        ConverterTileentity converter = (ConverterTileentity) tileEntity;

        player.openMenu(new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent(state.getBlock().getDescriptionId());
            }

            @Nullable
            @Override
            public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                return new VillagerIOContainer(id, playerInventory, converter.getInputInventory(), converter.getOutputInventory());
            }
        });

        return ActionResultType.SUCCESS;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof ConverterTileentity && placer != null) {
            ConverterTileentity converter = (ConverterTileentity) tile;
            converter.setOwner(placer.getUUID());
        }
        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new ConverterTileentity();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1F;
    }

}
