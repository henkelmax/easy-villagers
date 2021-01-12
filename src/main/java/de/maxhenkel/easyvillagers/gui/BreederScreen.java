package de.maxhenkel.easyvillagers.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BreederScreen extends InputOutputScreen<BreederContainer> {

    public BreederScreen(BreederContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected ITextComponent getTopText() {
        return new TranslationTextComponent("gui.easy_villagers.food_items");
    }

    @Override
    protected ITextComponent getBottomText() {
        return new TranslationTextComponent("gui.easy_villagers.output_items");
    }

}