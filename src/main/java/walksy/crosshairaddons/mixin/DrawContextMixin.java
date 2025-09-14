package walksy.crosshairaddons.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderPipeline;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin that intercepts DrawContext drawTexture calls.
 *
 * NOTE: the old method drawGuiTexture may not exist in current mappings.
 * We inject into drawTexture variants instead.
 */
@Mixin(DrawContext.class)
public class DrawContextMixin {

    // Example: intercept a drawTexture overload that takes a RenderPipeline first.
    // Exact descriptor will depend on your mappings. If injection fails,
    // search DrawContext.mapping for "drawTexture(" and copy the descriptor.
    @Inject(method = "drawTexture(Lnet/minecraft/client/render/RenderPipeline;Lnet/minecraft/util/Identifier;IIIIIIIII)V",
            at = @At("HEAD"), cancellable = true)
    private void onDrawTextureRenderPipelineFirst(RenderPipeline pipeline, Identifier id, int x, int y, int u, int v, int width, int height, int texW, int texH, int something, CallbackInfoReturnable<Void> cir) {
        // If you want to cancel or alter behavior for our custom id, do it here.
        // This is intentionally empty — example hook for future custom handling.
    }

    // Fallback: inject into identifier-first overloads.
    @Inject(method = "drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
            at = @At("HEAD"), cancellable = true)
    private void onDrawTextureIdentifierFirst(Identifier id, int x, int y, int u, int v, int width, int height, CallbackInfoReturnable<Void> cir) {
        // placeholder - no op
    }
}
