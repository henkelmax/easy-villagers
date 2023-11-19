package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TraderRenderer extends VillagerRendererBase<TraderTileentity> {

    private static final Minecraft mc = Minecraft.getInstance();

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
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(0D, 0D, -4D / 16D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            renderer.render(trader.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }

        if (trader.hasWorkstation()) {
            matrixStack.pushPose();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(0D, 0D, 2D / 16D);
            matrixStack.translate(-0.5D, 0D, -0.5D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            matrixStack.translate(0.5D / 0.45D - 0.5D, 0D, 0.5D / 0.45D - 0.5D);

            BlockState workstation = getState(trader.getWorkstation());

            getTransforms(workstation).accept(matrixStack);
            renderBlock(workstation, matrixStack, buffer, combinedLight, combinedOverlay);

            BlockState topBlock = getTopBlock(workstation);
            if (!topBlock.isAir()) {
                matrixStack.translate(0D, 1D, 0D);
                renderBlock(topBlock, matrixStack, buffer, combinedLight, combinedOverlay);
            }
            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

    public static void renderBlock(BlockState state, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        mc.getBlockRenderer().renderSingleBlock(state, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    public static BlockState getState(Block block) {
        return blockStateCache.get(block, () -> getFittingState(block));
    }

    protected static BlockState getFittingState(Block block) {
        if (block == Blocks.GRINDSTONE) {
            return block.defaultBlockState().setValue(GrindstoneBlock.FACE, AttachFace.FLOOR);
        }
        return block.defaultBlockState();
    }

    public static final Map<ResourceLocation, Consumer<PoseStack>> TRANSFORMS = new HashMap<>();
    private static final Map<Block, Consumer<PoseStack>> TRANSFORMS_CACHE = new HashMap<>();

    public static final Map<ResourceLocation, ResourceLocation> TOP_BLOCKS = new HashMap<>();
    private static final Map<Block, BlockState> TOP_BLOCK_CACHE = new HashMap<>();

    static {
        Consumer<PoseStack> immersiveEngineering = stack -> {
            stack.translate(-0.5D, 0D, 0D);
        };
        TRANSFORMS.put(new ResourceLocation("immersiveengineering", "workbench"), immersiveEngineering);
        TRANSFORMS.put(new ResourceLocation("immersiveengineering", "circuit_table"), immersiveEngineering);

        TOP_BLOCKS.put(new ResourceLocation("car", "gas_station"), new ResourceLocation("car", "gas_station_top"));
    }

    protected static Consumer<PoseStack> getTransforms(BlockState block) {
        Consumer<PoseStack> cached = TRANSFORMS_CACHE.get(block.getBlock());
        if (cached != null) {
            return cached;
        }
        Consumer<PoseStack> transform = TRANSFORMS.get(BuiltInRegistries.BLOCK.getKey(block.getBlock()));
        if (transform == null) {
            transform = (stack) -> {
            };
        }
        TRANSFORMS_CACHE.put(block.getBlock(), transform);
        return transform;
    }

    protected static BlockState getTopBlock(BlockState bottom) {
        BlockState cached = TOP_BLOCK_CACHE.get(bottom.getBlock());
        if (cached != null) {
            return cached;
        }
        ResourceLocation resourceLocation = TOP_BLOCKS.get(BuiltInRegistries.BLOCK.getKey(bottom.getBlock()));
        if (resourceLocation == null) {
            BlockState state = Blocks.AIR.defaultBlockState();
            TOP_BLOCK_CACHE.put(bottom.getBlock(), state);
            return state;
        }
        if (!BuiltInRegistries.BLOCK.containsKey(resourceLocation)) {
            BlockState state = Blocks.AIR.defaultBlockState();
            TOP_BLOCK_CACHE.put(bottom.getBlock(), state);
            return state;
        }
        Block b = BuiltInRegistries.BLOCK.get(resourceLocation);
        BlockState state = b.defaultBlockState();
        TOP_BLOCK_CACHE.put(bottom.getBlock(), state);
        return state;
    }

}