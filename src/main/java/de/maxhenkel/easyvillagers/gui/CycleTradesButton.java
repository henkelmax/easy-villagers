package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MerchantMenu;

import java.util.Collections;
import java.util.function.Consumer;

public class CycleTradesButton extends AbstractButton {

    private static final Identifier ARROW_BUTTON = Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "textures/gui/container/arrow_button.png");

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
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        visible = canCycle(screen.getMenu());
        if (isHovered) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_BUTTON, getX(), getY(), 0, 14, WIDTH, HEIGHT, 32, 32);
            guiGraphics.setTooltipForNextFrame(screen.getMinecraft().font, Collections.singletonList(Component.translatable("tooltip.easy_villagers.cycle_trades").getVisualOrderText()), mouseX, mouseY);
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_BUTTON, getX(), getY(), 0, 0, WIDTH, HEIGHT, 32, 32);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public static boolean canCycle(MerchantMenu menu) {
        return menu.showProgressBar() && menu.getTraderXp() <= 0 && menu.tradeContainer.getActiveOffer() == null;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        onPress.accept(this);
    }
}
