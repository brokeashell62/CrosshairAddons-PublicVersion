package walksy.crosshairaddons.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Inject(method = "drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIIII)V", at = @At("HEAD"), cancellable = true)
    private void onDrawGuiTexture(Identifier identifier, int x, int y, int width, int height, int z, CallbackInfo ci) {
        // Add custom rendering logic here
    }

    @Inject(method = "drawGuiTexture(Lnet/minecraft/util/Identifier;IIIII)V", at = @At("HEAD"), cancellable = true)
    private void onDrawGuiTextureShort(Identifier identifier, int x, int y, int width, int height, CallbackInfo ci) {
        // Add custom rendering logic here for shorter method signature
    }
}
