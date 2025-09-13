package walksy.crosshairaddons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Inject;
import org.spongepowered.asm.mixin.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(MatrixStack matrices, float tickDelta, long startTime, boolean tick, float frameTime, CallbackInfo ci) {
        DrawContext drawContext = new DrawContext(MinecraftClient.getInstance(), VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer()));
        // Adjusted method signature and handling
    }
}
