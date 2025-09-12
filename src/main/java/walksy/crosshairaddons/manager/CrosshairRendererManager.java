package walksy.crosshairaddons.manager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CrosshairRendererManager {

    // Correct Identifier for the texture
    private static final Identifier CUSTOM_MOD_ICONS = new Identifier("crosshairaddons", "textures/gui/modicons.png");

    // Method to render the crosshair
    public void renderCrosshair(DrawContext context) {
        // Enable blend mode for transparency
        RenderSystem.enableBlend();

        // Set shader color to white (or modify if you want a different color)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        // Get the transformation matrix for scaling/translation
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push(); // Save the current matrix

        // Apply transformations if needed (for example, scale or translate the texture)
        matrixStack.translate(0, 0, 0);  // Translate the texture (modify as needed)
        matrixStack.scale(1.0F, 1.0F, 1.0F); // Scale the texture if needed

        // Draw the texture at a given position with width and height
        context.drawTexture(CUSTOM_MOD_ICONS, 0, 0, 10, 0, 100, 100); // Adjust position and size

        // Reset blend mode and transformations
        RenderSystem.defaultBlendFunc();
        matrixStack.pop(); // Restore the previous matrix
    }
}
