package walksy.crosshairaddons.manager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.awt.*;

public class CrosshairRendererManager {
    private static final Identifier CUSTOM_MOD_ICONS = new Identifier("crosshairaddons", "modicons.png");

    public static void renderCustomIcon(DrawContext context, int x, int y, int u, int v, int width, int height) {
        context.getMatrices().push();

        // Enable blending for transparency
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Reset color (white, fully opaque)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        // Correct overload from mappings:
        // drawTexture(Identifier texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight)
        context.drawTexture(
                CUSTOM_MOD_ICONS,
                x, y,
                u, v,
                width, height,
                256, 256 // Assuming texture is 256x256, adjust if your PNG is different
        );

        context.getMatrices().pop();
    }

    public static void renderColoredIcon(DrawContext context, int x, int y, int u, int v, int width, int height, Color color) {
        context.getMatrices().push();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Apply color
        RenderSystem.setShaderColor(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                1f
        );

        context.drawTexture(
                CUSTOM_MOD_ICONS,
                x, y,
                u, v,
                width, height,
                256, 256
        );

        // Reset shader color back to white
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        context.getMatrices().pop();
    }
}
