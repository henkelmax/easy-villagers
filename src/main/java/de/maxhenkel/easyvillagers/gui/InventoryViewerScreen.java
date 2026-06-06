package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.easyvillagers.gui.ScreenBase;
import de.maxhenkel.easyvillagers.EasyVillagersMod;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class InventoryViewerScreen extends ScreenBase<InventoryViewerContainer> {

    public static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(EasyVillagersMod.MODID, "textures/gui/container/villager_inventory.png");

    public static final Component VILLAGER_INVENTORY = Component.translatable("gui.easy_villagers.villager_inventory");
    public static final Component VILLAGER_EQUIPMENT = Component.translatable("gui.easy_villagers.villager_equipment");

    protected final Inventory playerInventory;

    public InventoryViewerScreen(InventoryViewerContainer container, Inventory playerInventory, Component title) {
        super(BACKGROUND, container, playerInventory, title, 176, 182);
        this.playerInventory = playerInventory;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        drawCenteredText(guiGraphics, VILLAGER_INVENTORY, 9, fontColor);
        drawCenteredText(guiGraphics, VILLAGER_EQUIPMENT, 58, fontColor);
        guiGraphics.text(font, playerInventory.getDisplayName().getVisualOrderText(), 8, imageHeight - 96 + 3, fontColor, false);
    }

}
