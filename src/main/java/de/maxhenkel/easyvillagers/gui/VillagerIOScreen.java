package de.maxhenkel.easyvillagers.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class VillagerIOScreen extends InputOutputScreen<VillagerIOContainer> {

    public VillagerIOScreen(VillagerIOContainer container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
    }

    @Override
    protected MutableComponent getTopText() {
        return new TranslatableComponent("gui.easy_villagers.input_villagers");
    }

    @Override
    protected MutableComponent getBottomText() {
        return new TranslatableComponent("gui.easy_villagers.output_villagers");
    }

}