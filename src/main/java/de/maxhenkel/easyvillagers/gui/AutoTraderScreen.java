package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AutoTraderScreen extends ScreenBase<AutoTraderContainer> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/container/auto_trader.png");

    private final Inventory playerInventory;

    public AutoTraderScreen(AutoTraderContainer container, Inventory playerInventory, Component name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 202;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new ArrowButton(leftPos + 8, topPos + 19, true, button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(false));
        }).bounds(leftPos + 8, topPos + 19, 16, 20).build());

        addRenderableWidget(new ArrowButton(leftPos + imageWidth - 16 - 8, topPos + 19, false, button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(true));
        }).bounds(leftPos + imageWidth - 16 - 8, topPos + 19, 16, 20).build());
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        super.renderLabels(matrixStack, x, y);
        drawCenteredText(matrixStack, title, 6, FONT_COLOR);
        drawCenteredText(matrixStack, Component.translatable("gui.easy_villagers.input"), 45, FONT_COLOR);
        drawCenteredText(matrixStack, Component.translatable("gui.easy_villagers.output"), 77, FONT_COLOR);
        font.draw(matrixStack, playerInventory.getDisplayName(), 8F, (float) (imageHeight - 96 + 3), FONT_COLOR);
    }

    protected void drawCenteredText(PoseStack matrixStack, Component text, int y, int color) {
        int width = font.width(text);
        font.draw(matrixStack, text, imageWidth / 2F - width / 2F, y, color);
    }

}