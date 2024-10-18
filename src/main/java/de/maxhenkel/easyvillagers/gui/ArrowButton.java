package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ArrowButton extends AbstractButton {

    private static final ResourceLocation ARROW_LEFT = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/arrow_left.png");
    private static final ResourceLocation ARROW_RIGHT = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/arrow_right.png");

    private final boolean left;
    private final OnPress onPress;

    public ArrowButton(int x, int y, boolean left, OnPress onPress) {
        super(x, y, 16, 20, Component.empty());
        this.left = left;
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderWidget(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.blit(RenderType::guiTextured, left ? ARROW_LEFT : ARROW_RIGHT, getX(), getY() + 2, 0, 0, width, height, 16, 16);
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
