package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.client.RenderUtils;
import de.maxhenkel.easyvillagers.blocks.TraderBlock;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
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
        matrixStack.push();
        Direction direction = Direction.SOUTH;
        if (!trader.isFakeWorld()) {
            direction = trader.getBlockState().get(TraderBlock.FACING);
        }

        if (trader.getVillagerEntity() != null) {
            matrixStack.push();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.translate(0D, 0D, -4D / 16D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            renderer.render(trader.getVillagerEntity(), 0F, 1F, matrixStack, buffer, combinedLight);
            matrixStack.pop();
        }

        if (trader.hasWorkstation()) {
            matrixStack.push();

            matrixStack.translate(0.5D, 1D / 16D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            matrixStack.translate(0D, 0D, 2D / 16D);
            matrixStack.translate(-0.5D, 0D, -0.5D);
            matrixStack.scale(0.45F, 0.45F, 0.45F);
            matrixStack.translate(0.5D / 0.45D - 0.5D, 0D, 0.5D / 0.45D - 0.5D);

            BlockState workstation = trader.getWorkstation().getDefaultState();
            BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
            int color = minecraft.getBlockColors().getColor(workstation, null, null, 0);
            dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderTypeLookup.func_239221_b_(workstation)), workstation, dispatcher.getModelForState(workstation), RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            BlockState topBlock = getTopBlock(workstation);
            if (topBlock != null) {
                matrixStack.translate(0D, 1D, 0D);
                dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderTypeLookup.func_239221_b_(topBlock)), topBlock, dispatcher.getModelForState(topBlock), RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            }
            matrixStack.pop();
        }

        matrixStack.pop();
    }

    public static final Map<ResourceLocation, ResourceLocation> TOP_BLOCKS = new HashMap<>();
    private static final Map<ResourceLocation, BlockState> TOP_BLOCK_CACHE = new HashMap<>();

    static {
        TOP_BLOCKS.put(new ResourceLocation("car", "gas_station"), new ResourceLocation("car", "gas_station_top"));
    }

    @Nullable
    protected static BlockState getTopBlock(BlockState bottom) {
        ResourceLocation registryName = bottom.getBlock().getRegistryName();

        ResourceLocation resourceLocation = TOP_BLOCKS.get(registryName);
        if (resourceLocation == null) {
            return null;
        }
        BlockState cached = TOP_BLOCK_CACHE.get(registryName);
        if (cached != null) {
            return cached;
        }
        Block b = ForgeRegistries.BLOCKS.getValue(resourceLocation);
        if (b == null) {
            return null;
        }
        BlockState state = b.getDefaultState();
        TOP_BLOCK_CACHE.put(registryName, state);
        return state;
    }

}
