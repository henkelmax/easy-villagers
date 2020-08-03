package de.maxhenkel.easyvillagers.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;

public class BreederScreen extends ScreenBase<BreederContainer> {

    public static final ResourceLocation DEFAULT_IMAGE = new ResourceLocation(Main.MODID, "textures/gui/container/breeder.png");

    private PlayerInventory playerInventory;

    public BreederScreen(BreederContainer shulkerboxContainer, PlayerInventory playerInventory, ITextComponent name) {
        super(DEFAULT_IMAGE, shulkerboxContainer, playerInventory, name);
        this.playerInventory = playerInventory;
        xSize = 176;
        ySize = 164;
    }

    @Override
    protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {
        drawCentered(matrixStack, new TranslationTextComponent("gui.easy_villagers.food_items"), 9, FONT_COLOR);
        drawCentered(matrixStack, new TranslationTextComponent("gui.easy_villagers.output_items"), 40, FONT_COLOR);
        field_230712_o_.func_238422_b_(matrixStack, playerInventory.getDisplayName(), 8F, (float) (ySize - 96 + 3), FONT_COLOR);
    }

    private void drawCentered(MatrixStack matrixStack, ITextProperties text, int y, int color) {
        int width = field_230712_o_.getStringWidth(text.getString());
        field_230712_o_.func_238422_b_(matrixStack, text, xSize / 2F - width / 2F, y, color);
    }

}