package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;

public class CycleTradesButton extends Button {

    private static final ResourceLocation ARROW_BUTTON = new ResourceLocation(Main.MODID, "textures/gui/container/arrow_button.png");

    public static final int WIDTH = 18;
    public static final int HEIGHT = 14;

    private MerchantScreen screen;

    public CycleTradesButton(int x, int y, IPressable pressable, MerchantScreen screen) {
        super(x, y, WIDTH, HEIGHT, "", pressable);
        this.screen = screen;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        visible = screen.getContainer().func_217042_i() /* hasXpBar */ && screen.getContainer().getXp() <= 0;
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        Minecraft.getInstance().getTextureManager().bindTexture(ARROW_BUTTON);
        if (isHovered()) {
            blit(x, y, 0, 14, WIDTH, HEIGHT, 32, 32);
            screen.renderTooltip(Collections.singletonList(new TranslationTextComponent("tooltip.easy_villagers.cycle_trades").getString()), mouseX, mouseY, screen.getMinecraft().fontRenderer);
        } else {
            blit(x, y, 0, 0, WIDTH, HEIGHT, 32, 32);
        }
    }
}
