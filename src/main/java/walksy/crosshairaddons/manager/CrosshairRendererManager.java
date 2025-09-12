package walksy.crosshairaddons.manager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import walksy.crosshairaddons.CrosshairAddonsMod;

public class CrosshairRendererManager {

    // Updated Identifier creation
    private static final Identifier CUSTOM_MOD_ICONS = new Identifier("crosshairaddons", "textures/gui/modicons.png");

    public void renderCrosshair(DrawContext context) {
        // This method assumes 'context' is already initialized, such as within a rendering method.

        // Enable blend
        RenderSystem.enableBlend();
        
        // Using correct rendering method
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Set to white (or modify if needed)

        // Push and pop matrices to ensure proper transformations
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();

        // Example of translating and scaling texture (fixed matrix issue)
        matrixStack.translate(0, 0, 0);  // Ensure translation is applied correctly
        matrixStack.scale(1.0F, 1.0F, 1.0F); // Scale if needed

        // Draw texture at the proper location
        context.drawTexture(CUSTOM_MOD_ICONS, 0, 0, 10, 0, 100, 100);  // Adjust the size/position accordingly

        // Reset blend and transformations
        RenderSystem.defaultBlendFunc();
        matrixStack.pop();
    }
}
