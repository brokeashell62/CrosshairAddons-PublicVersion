package walksy.crosshairaddons.manager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class CrosshairRendererManager {

    private static final Identifier CUSTOM_MOD_ICONS = new Identifier("crosshairaddons", "modicons.png");
    private MinecraftClient client;
    
    public CrosshairRendererManager(MinecraftClient client) {
        this.client = client;
    }

    public void renderCrosshair(MatrixStack matrices, float scale, int originalWidth, int originalHeight) {
        // Push the current pose to the matrix stack
        matrices.push();

        // Set blend mode to enable transparent rendering
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        // Center the crosshair
        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;

        // Scale and translate the matrix to render the crosshair in the center
        matrices.translate(-(float) originalWidth / 2, -(float) originalHeight / 2 + 3.7f, 0);
        matrices.scale(scale, scale, 1.0F);

        // Draw the custom crosshair texture
        client.getTextureManager().bindTexture(CUSTOM_MOD_ICONS);
        RenderSystem.setShaderTexture(0, CUSTOM_MOD_ICONS);
        client.getBufferBuilders().getEntityVertexConsumers().drawQuad(centerX, centerY, originalWidth, originalHeight);

        // Reset the shader color and disable blend mode
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        // Pop the matrix to restore the previous transformations
        matrices.pop();
    }
}
