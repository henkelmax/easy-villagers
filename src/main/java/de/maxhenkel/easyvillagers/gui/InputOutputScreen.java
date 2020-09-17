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
        xSize = 176;
        ySize = 164;
    }

    @Override
    protected void drawForeground(MatrixStack matrixStack, int x, int y) {
        drawCentered(matrixStack, getTopText(), 9, FONT_COLOR);
        drawCentered(matrixStack, getBottomText(), 40, FONT_COLOR);
        textRenderer.draw(matrixStack, playerInventory.getDisplayName().asOrderedText(), 8F, (float) (ySize - 96 + 3), FONT_COLOR);
    }

    protected void drawCentered(MatrixStack matrixStack, IFormattableTextComponent text, int y, int color) {
        int width = textRenderer.getStringWidth(text.getString());
        textRenderer.draw(matrixStack, text.asOrderedText(), xSize / 2F - width / 2F, y, color);
    }

    protected abstract IFormattableTextComponent getTopText();

    protected abstract IFormattableTextComponent getBottomText();

}