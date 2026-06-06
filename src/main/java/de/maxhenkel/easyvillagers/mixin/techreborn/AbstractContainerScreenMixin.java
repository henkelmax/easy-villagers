package de.maxhenkel.easyvillagers.mixin.techreborn;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reborncore.client.gui.GuiBase;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Inject(method = "getHoveredSlot", at = @At("HEAD"), cancellable = true)
    private void easyvillagers$blockHoveredSlot(double mouseX, double mouseY, CallbackInfoReturnable<Slot> cir) {
        if ((Object) this instanceof GuiBase<?> guiBase) {
            if (guiBase.isTabOpen()) {
                reborncore.client.gui.config.GuiTab tab = guiBase.getSelectedTab();
                if (tab instanceof reborncore.client.gui.config.SlotConfigGui slotGui) {
                    if (slotGui.getSelectedSlot() != null) {
                        int popupX = guiBase.getGuiLeft() + slotGui.getSelectedSlot().getX() - 8;
                        int popupY = guiBase.getGuiTop() + slotGui.getSelectedSlot().getY() - 7;
                        int popupW = 84;
                        int popupH = 79;
                        if (mouseX >= popupX && mouseX < popupX + popupW && mouseY >= popupY && mouseY < popupY + popupH) {
                            cir.setReturnValue(null);
                        }
                    }
                }
            }
        } else if ((Object) this instanceof de.maxhenkel.easyvillagers.gui.ScreenBase<?> screenBase) {
            if (screenBase.trSlotConfigGui != null && de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.isTabOpen(screenBase.trSlotConfigGui)) {
                if (de.maxhenkel.easyvillagers.integration.techreborn.TRDummyIntegration.isMouseOverPopup(screenBase.trSlotConfigGui, mouseX, mouseY)) {
                    cir.setReturnValue(null);
                }
            }
        }
    }
}