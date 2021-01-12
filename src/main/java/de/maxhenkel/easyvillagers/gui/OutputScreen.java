package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class OutputScreen extends ScreenBase<OutputContainer> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/container/output.png");

    private PlayerInventory playerInventory;

    public OutputScreen(OutputContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        xSize = 176;
        ySize = 133;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        drawCentered(new TranslationTextComponent("gui.easy_villagers.output"), 9, FONT_COLOR);
        font.drawString(playerInventory.getDisplayName().getString(), 8F, (float) (ySize - 96 + 3), FONT_COLOR);
    }

    protected void drawCentered(ITextComponent text, int y, int color) {
        int width = font.getStringWidth(text.getString());
        font.drawString(text.getString(), xSize / 2F - width / 2F, y, color);
    }

}