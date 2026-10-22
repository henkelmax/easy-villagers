package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ArrowButton extends AbstractButton {

    private static final ResourceLocation ARROWS = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/container/auto_trader_arrows.png");

    private final boolean left;
    private final OnPress onPress;

    public ArrowButton(int x, int y, boolean left, OnPress onPress) {
        super(x, y, 16, 20, Component.empty());
        this.left = left;
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        guiGraphics.blit(ARROWS, getX(), getY(), left ? 0 : 16, isHoveredOrFocused() ? 20 : 0, width, height, 64, 64);
    }

    @Override
    public void onPress() {
        onPress.onPress(this);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput elementOutput) {

    }

    @FunctionalInterface
    public interface OnPress {
        void onPress(ArrowButton button);
    }

}
