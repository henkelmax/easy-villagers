package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ArrowButton extends AbstractButton {

    private static final Identifier ARROWS = Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "textures/gui/container/auto_trader_arrows.png");

    private final boolean left;
    private final OnPress onPress;

    public ArrowButton(int x, int y, boolean left, OnPress onPress) {
        super(x, y, 16, 20, Component.empty());
        this.left = left;
        this.onPress = onPress;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROWS, getX(), getY(), left ? 0 : 16, isHoveredOrFocused() ? 20 : 0, width, height, 64, 64);
    }

    @Override
    public void onPress(InputWithModifiers input) {
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
