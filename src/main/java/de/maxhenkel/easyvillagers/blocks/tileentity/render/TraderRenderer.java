package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class TraderRenderer extends VillagerRendererBase<TraderTileentity> {

    public TraderRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(TraderTileentity trader, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        super.render(trader, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        renderTraderBase(minecraft, villagerRenderer, trader, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    public static void renderTraderBase(Minecraft minecraft, VillagerRenderer renderer, TraderTileentityBase trader, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        Direction direction = Direction.SOUTH;
        if (!trader.isFakeWorld()) {
            direction = trader.getBlockState().getValue(TraderBlock.FACING);
        }

        if (trader.getVillagerEntity() != null) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(0D, 0D, -4D / 16D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            renderer.render(trader.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        if (trader.hasWorkstation()) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(0D, 0D, 2D / 16D);
            matrixStack.translate(-0.5D, 0D, -0.5D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            matrixStack.translate(0.5D / 0.45D - 0.5D, 0D, 0.5D / 0.45D - 0.5D);

            BlockState workstation = getState(trader.getWorkstation());
            BlockRendererDispatcher dispatcher = minecraft.getBlockRenderer();
            int color = minecraft.getBlockColors().getColor(workstation, null, null, 0);
            dispatcher.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderTypeLookup.getMovingBlockRenderType(workstation)), workstation, dispatcher.getBlockModel(workstation), RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            BlockState topBlock = getTopBlock(workstation);
            if (!topBlock.isAir()) {
                matrixStack.translate(0D, 1D, 0D);
                int topColor = minecraft.getBlockColors().getColor(topBlock, null, null, 0);
                dispatcher.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderTypeLookup.getMovingBlockRenderType(topBlock)), topBlock, dispatcher.getBlockModel(topBlock), RenderUtils.getRed(topColor), RenderUtils.getGreen(topColor), RenderUtils.getBlue(topColor), combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            }
            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

    private static final Map<Block, BlockState> BLOCK_CACHE = new HashMap<>();

    public static BlockState getState(Block block) {
        BlockState state = BLOCK_CACHE.get(block);
        if (state == null) {
            BlockState fittingState = getFittingState(block);
            BLOCK_CACHE.put(block, fittingState);
            return fittingState;
        } else {
            return state;
        }
    }

    protected static BlockState getFittingState(Block block) {
        if (block == Blocks.GRINDSTONE) {
            return block.defaultBlockState().setValue(GrindstoneBlock.FACE, AttachFace.FLOOR);
        }
        return block.defaultBlockState();
    }

    public static final Map<ResourceLocation, ResourceLocation> TOP_BLOCKS = new HashMap<>();
    private static final Map<Block, BlockState> TOP_BLOCK_CACHE = new HashMap<>();

    static {
        TOP_BLOCKS.put(new ResourceLocation("car", "gas_station"), new ResourceLocation("car", "gas_station_top"));
    }

    protected static BlockState getTopBlock(BlockState bottom) {
        BlockState cached = TOP_BLOCK_CACHE.get(bottom.getBlock());
        if (cached != null) {
            return cached;
        }
        ResourceLocation resourceLocation = TOP_BLOCKS.get(bottom.getBlock().getRegistryName());
        if (resourceLocation == null) {
            BlockState state = Blocks.AIR.defaultBlockState();
            TOP_BLOCK_CACHE.put(bottom.getBlock(), state);
            return state;
        }
        Block b = ForgeRegistries.BLOCKS.getValue(resourceLocation);
        if (b == null) {
            BlockState state = Blocks.AIR.defaultBlockState();
            TOP_BLOCK_CACHE.put(bottom.getBlock(), state);
            return state;
        }
        BlockState state = b.defaultBlockState();
        TOP_BLOCK_CACHE.put(bottom.getBlock(), state);
        return state;
    }

}
