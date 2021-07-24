package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OutputScreen extends ScreenBase<OutputContainer> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/container/output.png");

    private Inventory playerInventory;

    public OutputScreen(OutputContainer container, Inventory playerInventory, Component name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 133;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        drawCentered(matrixStack, new TranslatableComponent("gui.easy_villagers.output"), 9, FONT_COLOR);
        font.draw(matrixStack, playerInventory.getDisplayName(), 8F, (float) (imageHeight - 96 + 3), FONT_COLOR);
    }

    protected void drawCentered(PoseStack matrixStack, MutableComponent text, int y, int color) {
        int width = font.width(text.getString());
        font.draw(matrixStack, text, imageWidth / 2F - width / 2F, y, color);
    }

}