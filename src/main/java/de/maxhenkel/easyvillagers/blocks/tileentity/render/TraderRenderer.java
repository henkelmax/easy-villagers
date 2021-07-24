package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.corelib.helpers.Pair;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class TraderRenderer extends VillagerRendererBase<TraderTileentity> {

    private static final CachedMap<BlockState, Pair<BlockEntityRenderer<BlockEntity>, BlockEntity>> tileEntityCache = new CachedMap<>(10_000);
    private static final CachedMap<Block, BlockState> blockStateCache = new CachedMap<>(10_000);

    public TraderRenderer(BlockEntityRendererProvider.Context renderer) {
        super(renderer);
    }

    @Override
    public void render(TraderTileentity trader, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(trader, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        renderTraderBase(villagerRenderer, trader, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    public static void renderTraderBase(VillagerRenderer renderer, TraderTileentityBase trader, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
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

    public static void renderBlock(BlockState state, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (state.getBlock().getRenderShape(state) == RenderShape.MODEL) {
            BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            int color = Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
            RenderType renderType = ItemBlockRenderTypes.getRenderType(state, false);
            dispatcher.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(renderType), state, dispatcher.getBlockModel(state), RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), combinedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        } else {
            Pair<BlockEntityRenderer<BlockEntity>, BlockEntity> renderer = getRenderer(state);
            if (renderer != null) {
                renderer.getKey().render(renderer.getValue(), partialTicks, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    public static BlockState getState(Block block) {
        return blockStateCache.get(block, () -> getFittingState(block));
    }

    public static Pair<BlockEntityRenderer<BlockEntity>, BlockEntity> getRenderer(BlockState state) {
        return tileEntityCache.get(state, () -> {
            if (state.getBlock() instanceof EntityBlock entityBlock) {
                BlockEntity blockEntity = entityBlock.newBlockEntity(BlockPos.ZERO, state);
                if (blockEntity != null) {
                    BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);
                    if (renderer != null) {
                        return new Pair<>(renderer, blockEntity);
                    }
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