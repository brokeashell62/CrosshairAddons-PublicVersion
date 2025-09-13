package walksy.crosshairaddons.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Injection;
import org.spongepowered.asm.mixin.At;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Shadow
    private void drawGuiTexture(Identifier identifier, int x, int y, int width, int height) {
        // Implement the shadow method for rendering textures here
    }

    @Inject(method = "drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", at = @At("HEAD"), cancellable = true)
    private void injectDrawGuiTexture(Identifier identifier, int x, int y, int width, int height, CallbackInfo ci) {
        drawGuiTexture(identifier, x, y, width, height);
    }
}
