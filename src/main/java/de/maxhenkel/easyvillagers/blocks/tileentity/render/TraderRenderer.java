package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.corelib.helpers.Pair;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class TraderRenderer extends VillagerRendererBase<TraderTileentity> {

    private static final CachedMap<BlockState, Pair<TileEntityRenderer<TileEntity>, TileEntity>> tileEntityCache = new CachedMap<>(10_000);
    private static final CachedMap<Block, BlockState> blockStateCache = new CachedMap<>(10_000);

    public TraderRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(TraderTileentity trader, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        super.render(trader, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        renderTraderBase(villagerRenderer, trader, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    public static void renderTraderBase(VillagerRenderer renderer, TraderTileentityBase trader, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
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

            renderBlock(workstation, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);

            BlockState topBlock = getTopBlock(workstation);
            if (!topBlock.isAir()) {
                matrixStack.translate(0D, 1D, 0D);
                renderBlock(topBlock, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
            }
            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

    public static void renderBlock(BlockState state, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (state.getBlock().getRenderShape(state) == BlockRenderType.MODEL) {
            BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            int color = Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
            RenderType renderType = RenderTypeLookup.getRenderType(state, false);
            dispatcher.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(renderType), state, dispatcher.getBlockModel(state), RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), combinedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        } else {
            Pair<TileEntityRenderer<TileEntity>, TileEntity> renderer = getRenderer(state);
            if (renderer != null) {
                renderer.getKey().render(renderer.getValue(), partialTicks, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    public static BlockState getState(Block block) {
        return blockStateCache.get(block, () -> getFittingState(block));
    }

    public static Pair<TileEntityRenderer<TileEntity>, TileEntity> getRenderer(BlockState state) {
        return tileEntityCache.get(state, () -> {
            TileEntity tileEntity = state.createTileEntity(Minecraft.getInstance().level);
            if (tileEntity != null) {
                TileEntityRenderer<TileEntity> renderer = TileEntityRendererDispatcher.instance.getRenderer(tileEntity);
                if (renderer != null) {
                    tileEntity.blockState = state;
                    return new Pair<>(renderer, tileEntity);
                }
            }
            return null;
        });
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