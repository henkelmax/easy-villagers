package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AutoTraderScreen extends ScreenBase<AutoTraderContainer> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/container/auto_trader.png");

    private PlayerInventory playerInventory;

    public AutoTraderScreen(AutoTraderContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 202;
    }

    @Override
    protected void init() {
        super.init();

        addButton(new Button(leftPos + 8, topPos + 19, 16, 20, new StringTextComponent(""), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(false));
        }));

        addButton(new Button(leftPos + imageWidth - 16 - 8, topPos + 19, 16, 20, new StringTextComponent(""), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(true));
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        super.render(matrixStack, x, y, partialTicks);
        minecraft.getTextureManager().bind(BACKGROUND);
        blit(matrixStack, leftPos + 14, topPos + 25, 176, 7, 4, 7);
        blit(matrixStack, leftPos + imageWidth - 14 - 4, topPos + 25, 176, 0, 4, 7);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        super.renderLabels(matrixStack, x, y);
        drawCenteredText(matrixStack, title, 6, FONT_COLOR);
        drawCenteredText(matrixStack, new TranslationTextComponent("gui.easy_villagers.input"), 45, FONT_COLOR);
        drawCenteredText(matrixStack, new TranslationTextComponent("gui.easy_villagers.output"), 77, FONT_COLOR);
        font.draw(matrixStack, playerInventory.getDisplayName(), 8F, (float) (imageHeight - 96 + 3), FONT_COLOR);
    }

    protected void drawCenteredText(MatrixStack matrixStack, ITextComponent text, int y, int color) {
        int width = font.width(text);
        font.draw(matrixStack, text, imageWidth / 2F - width / 2F, y, color);
    }

}