package walksy.crosshairaddons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import walksy.crosshairaddons.manager.CrosshairRendererManager;

/**
 * Mixin into GameRenderer to hook into the HUD rendering and pass a DrawContext
 * to our CrosshairRendererManager.
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;

    // Intercept the method that renders the HUD. Exact injection depends on your GameRenderer mapping.
    // Many versions have a method named "render(Lnet/minecraft/client/util/math/MatrixStack;F)V" or similar.
    // To avoid fragile signature targeting here, we provide a simple example of how you might obtain a DrawContext
    // when you already have access to a DrawContext or can create one.

    // If your GameRenderer class has a method that receives DrawContext, inject there and call our renderer.
    // Example pseudo-injection (not binding to any real method signature here - update descriptor to actual method):
    //
    // @Inject(method = "renderInGameHud", at = @At("HEAD"))
    // private void onRenderHud(DrawContext drawContext, float tickDelta, CallbackInfo ci) {
    //     CrosshairRendererManager.renderIcons(drawContext, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), List.of(Color.RED));
    // }

    // Since exact method names vary: below is a helper method you can call from an injection in GameRenderer
    // (create a small injection that gets or constructs a DrawContext and then calls this helper).
    private void renderCrosshairAddons(DrawContext context) {
        int w = client.getWindow().getScaledWidth();
        int h = client.getWindow().getScaledHeight();

        // Example color list; in real mod read from config
        java.awt.Color c = java.awt.Color.WHITE;
        CrosshairRendererManager.renderIcons(context, w, h, java.util.List.of(c));
    }
}
