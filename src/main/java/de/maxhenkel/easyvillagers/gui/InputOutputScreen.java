package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

public abstract class InputOutputScreen<T extends Container> extends ScreenBase<T> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/container/input_output.png");

    private PlayerInventory playerInventory;

    public InputOutputScreen(T container, PlayerInventory playerInventory, ITextComponent name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 164;
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        drawCentered(matrixStack, getTopText(), 9, FONT_COLOR);
        drawCentered(matrixStack, getBottomText(), 40, FONT_COLOR);
        font.draw(matrixStack, playerInventory.getDisplayName(), 8F, (float) (imageHeight - 96 + 3), FONT_COLOR);
    }

    protected void drawCentered(MatrixStack matrixStack, IFormattableTextComponent text, int y, int color) {
        int width = font.width(text);
        font.draw(matrixStack, text, imageWidth / 2F - width / 2F, y, color);
    }

    protected abstract IFormattableTextComponent getTopText();

    protected abstract IFormattableTextComponent getBottomText();

}