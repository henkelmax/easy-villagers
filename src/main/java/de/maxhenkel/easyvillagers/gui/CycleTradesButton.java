package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MerchantMenu;

import java.util.Collections;
import java.util.function.Consumer;

public class CycleTradesButton extends AbstractButton {

    private static final ResourceLocation ARROW_BUTTON = new ResourceLocation(Main.MODID, "textures/gui/container/arrow_button.png");

    public static final int WIDTH = 18;
    public static final int HEIGHT = 14;

    private MerchantScreen screen;
    private Consumer<CycleTradesButton> onPress;

    public CycleTradesButton(int x, int y, Consumer<CycleTradesButton> onPress, MerchantScreen screen) {
        super(x, y, WIDTH, HEIGHT, Component.empty());
        this.onPress = onPress;
        this.screen = screen;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        visible = canCycle(screen.getMenu());
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        if (isHovered) {
            guiGraphics.blit(ARROW_BUTTON, getX(), getY(), 0, 14, WIDTH, HEIGHT, 32, 32);
            guiGraphics.renderTooltip(screen.getMinecraft().font, Collections.singletonList(Component.translatable("tooltip.easy_villagers.cycle_trades").getVisualOrderText()), mouseX, mouseY);
        } else {
            guiGraphics.blit(ARROW_BUTTON, getX(), getY(), 0, 0, WIDTH, HEIGHT, 32, 32);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public static boolean canCycle(MerchantMenu menu) {
        return menu.showProgressBar() && menu.getTraderXp() <= 0 && menu.tradeContainer.getActiveOffer() == null;
    }

    @Override
    public void onPress() {
        onPress.accept(this);
    }
}
