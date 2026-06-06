package de.maxhenkel.easyvillagers.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ScreenBase<T extends net.minecraft.world.inventory.AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected Identifier texture;
    protected int fontColor = -12566463;

    public ScreenBase(Identifier texture, T container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.texture = texture;
    }

    public ScreenBase(Identifier texture, T container, Inventory playerInventory, Component title, int width, int height) {
        super(container, playerInventory, title, width, height);
        this.texture = texture;
    }

    public int getLeftPos() { return leftPos; }
    public int getTopPos() { return topPos; }

    public Object trSlotConfigGui = null;

    @Override
    protected void init() {
        super.init();
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("techreborn")) {
            try {
                if (getMenu() instanceof de.maxhenkel.easyvillagers.gui.VillagerContainerBase cb && cb.getBlockEntity() instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible) {
                    Object configObj = null; net.minecraft.core.BlockPos pos = net.minecraft.core.BlockPos.ZERO; net.minecraft.world.level.block.entity.BlockEntityType<?> type = null; net.minecraft.world.level.block.state.BlockState state = null; if (true) { if (cb.getBlockEntity() instanceof de.maxhenkel.easyvillagers.integration.techreborn.ITRCompatible itr && !(cb.getBlockEntity() instanceof de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity)) { configObj = itr.getSlotConfiguration(); pos = cb.getBlockEntity().getBlockPos(); type = cb.getBlockEntity().getType(); state = cb.getBlockEntity().getBlockState(); } } trSlotConfigGui = de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.createSlotConfigGui(this, type, pos, state, configObj);
                    de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.open(trSlotConfigGui);
                }
            } catch (Exception e) {
                e.printStackTrace();
                }
        }
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        if (trSlotConfigGui != null) {
            if (de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.click(trSlotConfigGui, event.x(), event.y(), event.button(), leftPos, topPos)) { 
                return true; 
            } 
        } 
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    protected boolean isHovering(int left, int top, int w, int h, double xm, double ym) {
        if (trSlotConfigGui != null && de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.isTabOpen(trSlotConfigGui)) {
            if (de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.isMouseOverPopup(trSlotConfigGui, xm, ym)) {
                return false;
            }
        }
        return super.isHovering(left, top, w, h, xm, ym);
    }

    @Override
    public boolean mouseReleased(net.minecraft.client.input.MouseButtonEvent event) {
        if (trSlotConfigGui != null) {
            de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.mouseReleased(trSlotConfigGui, event.x(), event.y(), event.button());
            if (de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.isMouseOverPopup(trSlotConfigGui, event.x(), event.y())) {
                return true;
            }
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event, double dragX, double dragY) {
        if (trSlotConfigGui != null) {
            if (de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.isMouseOverPopup(trSlotConfigGui, event.x(), event.y())) {
                return true;
            }
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        
        if (trSlotConfigGui != null && de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.isTabOpen(trSlotConfigGui)) {
            de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.draw(trSlotConfigGui, guiGraphics, mouseX, mouseY, leftPos, topPos);
        } else {
            this.extractTooltip(guiGraphics, mouseX, mouseY);
            if (trSlotConfigGui != null) {
                de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.draw(trSlotConfigGui, guiGraphics, mouseX, mouseY, leftPos, topPos);
            }
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        if (trSlotConfigGui != null && de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.isTabOpen(trSlotConfigGui)) {
            return;
        }
        super.extractTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (texture != null) {
            guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        guiGraphics.text(font, title.getString(), titleLabelX, titleLabelY, fontColor, false);
        guiGraphics.text(font, playerInventoryTitle.getString(), inventoryLabelX, inventoryLabelY, fontColor, false);
    }

    protected void drawCenteredText(GuiGraphicsExtractor guiGraphics, Component text, int y, int color) {
        int width = font.width(text);
        guiGraphics.text(font, text, imageWidth / 2 - width / 2, y, color, false);
    }
}