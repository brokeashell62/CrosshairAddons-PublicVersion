package walksy.crosshairaddons.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    // Shadow the original method for drawing textures (we don't need to implement it)
    @Shadow
    private void drawGuiTexture(Identifier identifier, int x, int y, int width, int height) {
        // This is where the actual Minecraft method would be
    }

    // Inject into the drawGuiTexture method to customize how the texture is drawn
    @Inject(method = "drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", at = @At("HEAD"), cancellable = true)
    private void injectDrawGuiTexture(Identifier identifier, int x, int y, int width, int height, CallbackInfo ci) {
        // Custom logic for drawing the texture can be added here, if necessary.
        // For example, we can modify how textures are drawn based on conditions.
    }
}
