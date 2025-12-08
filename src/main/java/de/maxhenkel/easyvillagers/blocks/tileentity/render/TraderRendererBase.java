package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.maxhenkel.corelib.CachedMap;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
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

    private static final CachedMap<Block, BlockState> blockStateCache = new CachedMap<>(10_000);

    private final BlockRenderDispatcher blockRenderer;

    public TraderRendererBase(EntityModelSet entityModelSet, BlockRenderDispatcher blockRenderer) {
        super(entityModelSet);
        this.blockRenderer = blockRenderer;
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
            state.workstation = getState(trader.getWorkstation());
        } else {
            state.workstation = null;
        }
    }

    @Override
    public void submit(TraderRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        submitTraderBase(getVillagerRenderer(), blockRenderer, state, stack, collector, cameraRenderState);
    }

    public static void submitTraderBase(VillagerRenderer villagerRenderer, BlockRenderDispatcher blockRenderer, TraderRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
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

        if (state.workstation != null) {
            stack.pushPose();

            stack.translate(0.5D, 1D / 16D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
            stack.translate(0D, 0D, 2D / 16D);
            stack.translate(-0.5D, 0D, -0.5D);
            stack.scale(0.45F, 0.45F, 0.45F);
            stack.translate(0.5D / 0.45D - 0.5D, 0D, 0.5D / 0.45D - 0.5D);

            getTransforms(state.workstation).accept(stack);
            renderBlock(blockRenderer, state.workstation, state.lightCoords, stack, collector);

            BlockState topBlock = getTopBlock(state.workstation);
            if (!topBlock.isAir()) {
                stack.translate(0D, 1D, 0D);
                renderBlock(blockRenderer, topBlock, state.lightCoords, stack, collector);
            }
            stack.popPose();
        }

        stack.popPose();
    }

    public static void renderBlock(BlockRenderDispatcher blockRenderer, BlockState state, int lightCoords, PoseStack stack, SubmitNodeCollector collector) {
        int color = minecraft.getBlockColors().getColor(state, null, null, 0);
        // See ItemFrameRenderer
        collector.submitBlockModel(
                stack,
                RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS),
                blockRenderer.getBlockModel(state),
                RenderUtils.getRedFloat(color),
                RenderUtils.getGreenFloat(color),
                RenderUtils.getBlueFloat(color),
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                0
        );
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