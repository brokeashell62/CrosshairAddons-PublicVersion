package walksy.crosshairaddons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow private MinecraftClient client;
    @Shadow private VertexConsumerProvider.Immediate buffers;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (client != null && client.player != null) {
            DrawContext drawContext = new DrawContext(client, buffers);
            // You can call CrosshairRendererManager.renderCustomIcon() here if needed
        }
    }
}
