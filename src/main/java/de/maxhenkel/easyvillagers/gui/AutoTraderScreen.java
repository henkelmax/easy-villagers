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
        xSize = 176;
        ySize = 202;
    }

    @Override
    protected void init() {
        super.init();

        addButton(new Button(guiLeft + 8, guiTop + 19, 16, 20, new StringTextComponent(""), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(false));
        }));

        addButton(new Button(guiLeft + xSize - 16 - 8, guiTop + 19, 16, 20, new StringTextComponent(""), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(true));
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        super.render(matrixStack, x, y, partialTicks);
        minecraft.getTextureManager().bindTexture(BACKGROUND);
        blit(matrixStack, guiLeft + 14, guiTop + 25, 176, 7, 4, 7);
        blit(matrixStack, guiLeft + xSize - 14 - 4, guiTop + 25, 176, 0, 4, 7);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        drawCentered(matrixStack, title, 6, FONT_COLOR);
        drawCentered(matrixStack, new TranslationTextComponent("gui.easy_villagers.input"), 45, FONT_COLOR);
        drawCentered(matrixStack, new TranslationTextComponent("gui.easy_villagers.output"), 77, FONT_COLOR);
        font.func_243248_b(matrixStack, playerInventory.getDisplayName(), 8F, (float) (ySize - 96 + 3), FONT_COLOR);
    }

    protected void drawCentered(MatrixStack matrixStack, ITextComponent text, int y, int color) {
        int width = font.getStringPropertyWidth(text);
        font.func_243248_b(matrixStack, text, xSize / 2F - width / 2F, y, color);
    }

}