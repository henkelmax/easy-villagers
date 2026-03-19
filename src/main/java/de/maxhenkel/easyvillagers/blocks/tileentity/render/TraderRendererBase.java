package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TraderRendererBase<T extends TraderTileentityBase> extends VillagerRendererBase<T, TraderRenderState> {

    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private static final CachedMap<Block, BlockState> blockStateCache = new CachedMap<>(10_000);

    private final BlockModelResolver blockModelResolver;

    public TraderRendererBase(EntityModelSet entityModelSet, BlockModelResolver blockModelResolver) {
        super(entityModelSet);
        this.blockModelResolver = blockModelResolver;
    }

    @Override
    public TraderRenderState createRenderState() {
        return new TraderRenderState();
    }

    @Override
    public void extractRenderState(T trader, TraderRenderState state, float partialTicks, Vec3 pos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(trader, state, partialTicks, pos, crumblingOverlay);

        state.apply(trader);

        state.renderVillager = false;
        if (trader.getVillagerEntity() != null) {
            state.renderVillager = true;
            VillagerRenderer villagerRenderer = getVillagerRenderer();
            villagerRenderer.extractRenderState(trader.getVillagerEntity(), state.villagerRenderState, partialTicks);
            state.villagerRenderState.lightCoords = getLightOrDefault(trader, state);
        }

        if (trader.hasWorkstation()) {
            BlockState workstationState = getState(trader.getWorkstation());
            blockModelResolver.update(state.workstation, workstationState, BLOCK_DISPLAY_CONTEXT);
            state.blockTransforms = getTransforms(workstationState);
            BlockState topBlock = getTopBlock(workstationState);
            if (!topBlock.isAir()) {
                blockModelResolver.update(state.topBlock, topBlock, BLOCK_DISPLAY_CONTEXT);
            } else {
                state.topBlock.clear();
            }
        } else {
            state.workstation.clear();
            state.topBlock.clear();
        }
    }

    @Override
    public void submit(TraderRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        submitTraderBase(getVillagerRenderer(), state, stack, collector, cameraRenderState);
    }

    public static void submitTraderBase(VillagerRenderer villagerRenderer, TraderRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();

        if (state.renderVillager) {
            stack.pushPose();

            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(0D, 0D, -4D / 16D);
            stack.scale(0.45F, 0.45F, 0.45F);
            villagerRenderer.submit(state.villagerRenderState, stack, collector, cameraRenderState);
            stack.popPose();
        }

        if (!state.workstation.isEmpty()) {
            stack.pushPose();

            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(0D, 0D, 2D / 16D);
            stack.translate(-0.5D, 0D, -0.5D);
            stack.scale(0.45F, 0.45F, 0.45F);
            stack.translate(0.5D / 0.45D - 0.5D, 0D, 0.5D / 0.45D - 0.5D);

            if (state.blockTransforms != null) {
                state.blockTransforms.accept(stack);
            }
            renderBlock(state.workstation, state.lightCoords, stack, collector);

            if (!state.topBlock.isEmpty()) {
                stack.translate(0D, 1D, 0D);
                renderBlock(state.topBlock, state.lightCoords, stack, collector);
            }
            stack.popPose();
        }

        stack.popPose();
    }

    public static void renderBlock(BlockModelRenderState state, int lightCoords, PoseStack stack, SubmitNodeCollector collector) {
        state.submit(stack, collector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
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

    public static final Map<Identifier, Consumer<PoseStack>> TRANSFORMS = new HashMap<>();
    private static final Map<Block, Consumer<PoseStack>> TRANSFORMS_CACHE = new HashMap<>();

    public static final Map<Identifier, Identifier> TOP_BLOCKS = new HashMap<>();
    private static final Map<Block, BlockState> TOP_BLOCK_CACHE = new HashMap<>();

    static {
        Consumer<PoseStack> immersiveEngineering = stack -> {
            stack.translate(-0.5D, 0D, 0D);
        };
        TRANSFORMS.put(Identifier.fromNamespaceAndPath("immersiveengineering", "workbench"), immersiveEngineering);
        TRANSFORMS.put(Identifier.fromNamespaceAndPath("immersiveengineering", "circuit_table"), immersiveEngineering);

        TOP_BLOCKS.put(Identifier.fromNamespaceAndPath("car", "gas_station"), Identifier.fromNamespaceAndPath("car", "gas_station_top"));
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
        Identifier resourceLocation = TOP_BLOCKS.get(BuiltInRegistries.BLOCK.getKey(bottom.getBlock()));
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
        Block b = BuiltInRegistries.BLOCK.get(resourceLocation).map(Holder.Reference::value).orElse(Blocks.AIR);
        BlockState state = b.defaultBlockState();
        TOP_BLOCK_CACHE.put(bottom.getBlock(), state);
        return state;
    }


}