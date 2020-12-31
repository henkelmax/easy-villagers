package de.maxhenkel.easyvillagers.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class IncubatorScreen extends InputOutputScreen<IncubatorContainer> {

    public IncubatorScreen(IncubatorContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected IFormattableTextComponent getTopText() {
        return new TranslationTextComponent("gui.easy_villagers.input_villagers");
    }

    @Override
    protected IFormattableTextComponent getBottomText() {
        return new TranslationTextComponent("gui.easy_villagers.output_villagers");
    }

}