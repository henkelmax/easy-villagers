package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
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

        addButton(new Button(guiLeft + 8, guiTop + 19, 16, 20, "", button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(false));
        }));

        addButton(new Button(guiLeft + xSize - 16 - 8, guiTop + 19, 16, 20, "", button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(true));
        }));
    }

    @Override
    public void render(int x, int y, float partialTicks) {
        super.render(x, y, partialTicks);
        minecraft.getTextureManager().bindTexture(BACKGROUND);
        blit(guiLeft + 14, guiTop + 25, 176, 7, 4, 7);
        blit(guiLeft + xSize - 14 - 4, guiTop + 25, 176, 0, 4, 7);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
        drawCentered(title.getString(), 6, FONT_COLOR);
        drawCentered(new TranslationTextComponent("gui.easy_villagers.input").getString(), 45, FONT_COLOR);
        drawCentered(new TranslationTextComponent("gui.easy_villagers.output").getString(), 77, FONT_COLOR);
        font.drawString(playerInventory.getDisplayName().getString(), 8F, (float) (ySize - 96 + 3), FONT_COLOR);
    }

    protected void drawCentered(String text, int y, int color) {
        int width = font.getStringWidth(text);
        font.drawString(text, xSize / 2F - width / 2F, y, color);
    }

}