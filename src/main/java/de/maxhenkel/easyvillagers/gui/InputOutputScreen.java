package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class InputOutputScreen<T extends AbstractContainerMenu> extends ScreenBase<T> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/container/input_output.png");

    private Inventory playerInventory;

    public InputOutputScreen(T container, Inventory playerInventory, Component name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 164;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        drawCentered(matrixStack, getTopText(), 9, FONT_COLOR);
        drawCentered(matrixStack, getBottomText(), 40, FONT_COLOR);
        font.draw(matrixStack, playerInventory.getDisplayName(), 8F, (float) (imageHeight - 96 + 3), FONT_COLOR);
    }

    protected void drawCentered(PoseStack matrixStack, MutableComponent text, int y, int color) {
        int width = font.width(text);
        font.draw(matrixStack, text, imageWidth / 2F - width / 2F, y, color);
    }

    protected abstract MutableComponent getTopText();

    protected abstract MutableComponent getBottomText();

}