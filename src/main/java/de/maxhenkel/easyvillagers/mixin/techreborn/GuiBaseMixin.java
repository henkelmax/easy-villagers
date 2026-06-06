package de.maxhenkel.easyvillagers.mixin.techreborn;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reborncore.client.gui.GuiBase;

@Mixin(value = GuiBase.class, remap = false)
public abstract class GuiBaseMixin {

    @Shadow
    public abstract boolean isTabOpen();

    @Inject(method = "extractTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;extractTooltip(Lnet/minecraft/client/gui/GuiGraphicsExtractor;II)V", remap = true), cancellable = true)
    private void easyvillagers$injectExtractTooltip(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.isTabOpen()) {
            ci.cancel();
        }
    }
}