package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OutputScreen extends ScreenBase<OutputContainer> {

    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/container/output.png");

    private Inventory playerInventory;

    public OutputScreen(OutputContainer container, Inventory playerInventory, Component name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 133;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        drawCentered(guiGraphics, Component.translatable("gui.easy_villagers.output"), 9, FONT_COLOR);
        guiGraphics.drawString(font, playerInventory.getDisplayName().getVisualOrderText(), 8, imageHeight - 96 + 3, FONT_COLOR, false);
    }

    protected void drawCentered(GuiGraphics guiGraphics, MutableComponent text, int y, int color) {
        int width = font.width(text);
        guiGraphics.drawString(font, text.getVisualOrderText(), imageWidth / 2 - width / 2, y, color, false);
    }

}