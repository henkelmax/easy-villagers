package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class InventoryViewerScreen extends ScreenBase<InventoryViewerContainer> {

    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/container/villager_inventory.png");

    public static final Component VILLAGER_INVENTORY = Component.translatable("gui.easy_villagers.villager_inventory");
    public static final Component VILLAGER_EQUIPMENT = Component.translatable("gui.easy_villagers.villager_equipment");

    protected final Inventory playerInventory;

    public InventoryViewerScreen(InventoryViewerContainer container, Inventory playerInventory, Component title) {
        super(BACKGROUND, container, playerInventory, title);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 182;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        drawCentered(guiGraphics, VILLAGER_INVENTORY, 9, FONT_COLOR);
        drawCentered(guiGraphics, VILLAGER_EQUIPMENT, 58, FONT_COLOR);
        guiGraphics.drawString(font, playerInventory.getDisplayName().getVisualOrderText(), 8, imageHeight - 96 + 3, FONT_COLOR, false);
    }

}