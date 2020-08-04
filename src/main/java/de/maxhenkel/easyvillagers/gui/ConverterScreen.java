package de.maxhenkel.easyvillagers.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;

public class ConverterScreen extends InputOutputScreen<ConverterContainer> {

    public ConverterScreen(ConverterContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected ITextProperties getTopText() {
        return new TranslationTextComponent("gui.easy_villagers.input_villagers");
    }

    @Override
    protected ITextProperties getBottomText() {
        return new TranslationTextComponent("gui.easy_villagers.output_villagers");
    }

}