package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
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
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        drawCentered(getTopText().getString(), 9, FONT_COLOR);
        drawCentered(getBottomText().getString(), 40, FONT_COLOR);
        font.drawString(playerInventory.getDisplayName().getString(), 8F, (float) (ySize - 96 + 3), FONT_COLOR);
    }

    protected void drawCentered(String text, int y, int color) {
        int width = font.getStringWidth(text);
        font.drawString(text, xSize / 2F - width / 2F, y, color);
    }

    protected abstract ITextComponent getTopText();

    protected abstract ITextComponent getBottomText();

}