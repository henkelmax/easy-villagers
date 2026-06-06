package de.maxhenkel.easyvillagers.mixin.techreborn;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.config.elements.AbstractConfigPopupElement;
import reborncore.client.gui.config.elements.ElementBase;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import de.maxhenkel.easyvillagers.EasyVillagersMod;

@Mixin(value = AbstractConfigPopupElement.class, remap = false)
public abstract class AbstractConfigPopupElementMixin {

    @Shadow
    private int[][] mouseBoxMap;

    @Inject(method = "draw", at = @At("TAIL"))
    private void easyvillagers$injectDrawFaceTooltips(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int mouseX, int mouseY, CallbackInfo ci) {
        boolean showInAll = EasyVillagersMod.CONFIG.client.showFacesInAllConfigPanels.get();
        boolean showInEV = EasyVillagersMod.CONFIG.client.showFacesInConfigPanel.get();
        
        reborncore.common.blockentity.MachineBaseBlockEntity machine = gui.getMachine();
        boolean isDummy = machine != null && machine.getClass().getName().contains("DummyMachineBlockEntity");

        if (showInAll || (showInEV && isDummy)) {
            String[] faceNames = new String[]{"Up", "Front", "Right", "Left", "Down", "Back"};
            for (int i = 0; i < 6; i++) {
                int rectX = mouseBoxMap[i][0] + ((ElementBase) (Object) this).getX();
                int rectY = mouseBoxMap[i][1] + ((ElementBase) (Object) this).getY();
                boolean isIn = ElementBase.isInRect(gui, rectX, rectY, 16, 16, mouseX, mouseY);
                if (isIn) {
                    drawContext.setTooltipForNextFrame(
                        gui.getFont(),
                        java.util.List.of(Component.literal(faceNames[i]).getVisualOrderText()),
                        net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner.INSTANCE,
                        mouseX, mouseY,
                        true
                    );
                    break;
                }
            }
        }
    }
}