package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AutoTraderScreen extends ScreenBase<AutoTraderContainer> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/container/auto_trader.png");

    private Inventory playerInventory;

    public AutoTraderScreen(AutoTraderContainer container, Inventory playerInventory, Component name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 202;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new Button(leftPos + 8, topPos + 19, 16, 20, TextComponent.EMPTY, button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(false));
        }));

        addRenderableWidget(new Button(leftPos + imageWidth - 16 - 8, topPos + 19, 16, 20, TextComponent.EMPTY, button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(true));
        }));
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y, float partialTicks) {
        super.render(matrixStack, x, y, partialTicks);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        blit(matrixStack, leftPos + 14, topPos + 25, 176, 7, 4, 7);
        blit(matrixStack, leftPos + imageWidth - 14 - 4, topPos + 25, 176, 0, 4, 7);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        super.renderLabels(matrixStack, x, y);
        drawCenteredText(matrixStack, title, 6, FONT_COLOR);
        drawCenteredText(matrixStack, new TranslatableComponent("gui.easy_villagers.input"), 45, FONT_COLOR);
        drawCenteredText(matrixStack, new TranslatableComponent("gui.easy_villagers.output"), 77, FONT_COLOR);
        font.draw(matrixStack, playerInventory.getDisplayName(), 8F, (float) (imageHeight - 96 + 3), FONT_COLOR);
    }

    protected void drawCenteredText(PoseStack matrixStack, Component text, int y, int color) {
        int width = font.width(text);
        font.draw(matrixStack, text, imageWidth / 2F - width / 2F, y, color);
    }

}