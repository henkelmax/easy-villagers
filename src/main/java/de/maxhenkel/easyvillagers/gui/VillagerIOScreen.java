package de.maxhenkel.easyvillagers.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class VillagerIOScreen extends InputOutputScreen<VillagerIOContainer> {

    public VillagerIOScreen(VillagerIOContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected ITextComponent getTopText() {
        return new TranslationTextComponent("gui.easy_villagers.input_villagers");
    }

    @Override
    protected ITextComponent getBottomText() {
        return new TranslationTextComponent("gui.easy_villagers.output_villagers");
    }

}