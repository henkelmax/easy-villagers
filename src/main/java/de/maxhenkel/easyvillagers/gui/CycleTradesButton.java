package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;

public class CycleTradesButton extends Button {

    private static final ResourceLocation ARROW_BUTTON = new ResourceLocation(Main.MODID, "textures/gui/container/arrow_button.png");

    public static final int WIDTH = 18;
    public static final int HEIGHT = 14;

    private MerchantScreen screen;

    public CycleTradesButton(int x, int y, OnPress pressable, MerchantScreen screen) {
        super(x, y, WIDTH, HEIGHT, TextComponent.EMPTY, pressable);
        this.screen = screen;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        visible = screen.getMenu().showProgressBar() && screen.getMenu().getTraderXp() <= 0;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, ARROW_BUTTON);
        if (isHovered()) {
            blit(matrixStack, x, y, 0, 14, WIDTH, HEIGHT, 32, 32);
            screen.renderTooltip(matrixStack, Collections.singletonList(new TranslatableComponent("tooltip.easy_villagers.cycle_trades").getVisualOrderText()), mouseX, mouseY, screen.getMinecraft().font);
        } else {
            blit(matrixStack, x, y, 0, 0, WIDTH, HEIGHT, 32, 32);
        }
    }
}
