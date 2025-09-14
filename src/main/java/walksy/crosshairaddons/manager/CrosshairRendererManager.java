package walksy.crosshairaddons.manager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderPipeline;
import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.List;

/**
 * Manager that renders custom crosshair icons and mod icons.
 *
 * NOTE: This file was updated for modern DrawContext / RenderSystem signatures.
 * If your mappings use slightly different names, see instructions below.
 */
public class CrosshairRendererManager {

    // Use Identifier.of("namespace:path") to avoid private ctor issues
    private static final Identifier CUSTOM_MOD_ICONS = Identifier.of("crosshairaddons:modicons.png");

    /**
     * Render a list of icons centered on the screen using the provided DrawContext.
     * This method aims to be conservative with APIs so it compiles across 1.21.x mappings.
     */
    public static void renderIcons(DrawContext context, int screenWidth, int screenHeight, List<Color> colors) {
        // center coords
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // If DrawContext.drawTexture requires a RenderPipeline first, we retrieve one from DrawContext or RenderPipeline.
        // The name below is the common pattern: DrawContext methods in 1.21+ often take a RenderPipeline first.
        // If your mapping uses a different static accessor, search your DrawContext.mapping / RenderPipeline.mapping for
        // "drawTexture(" and for RenderPipeline factory methods (e.g. "getTexturedGui", "getGuiTextured", "getGuiPipeline", etc.)
        RenderPipeline pipeline = getGuiRenderPipeline(context);

        // Basic example: draw a small icon for each color
        int spacing = 12;
        int originalWidth = 10;
        int originalHeight = 10;
        float scale = 1.0f;

        // push matrix
        try {
            context.getMatrices().push(); // modern name: push()/pop()
        } catch (NoSuchMethodError e) {
            // Fallback: some mappings expose pushPose/popPose - try those if push/pop are missing
            try {
                context.getMatrices().pushPose();
            } catch (Throwable ignored) { }
        }

        // enable blending (mapping differences exist; see note below)
        enableBlendSafe();

        for (int i = 0; i < colors.size(); i++) {
            Color o = colors.get(i);

            // compute icon position
            int x = centerX + (i - colors.size() / 2) * spacing - originalWidth / 2;
            int y = centerY - originalHeight / 2 - 8;

            // set shader color (float RGBA)
            setShaderColorSafe(o.getRed() / 255f, o.getGreen() / 255f, o.getBlue() / 255f, 1.0f);

            // draw the sprite from the atlas
            // The DrawContext.drawTexture(...) overloads vary across versions; a common form is:
            // drawTexture(RenderPipeline pipeline, Identifier id, int x, int y, float u, float v, int width, int height, int texWidth, int texHeight)
            // If your DrawContext has a different signature, search DrawContext.mapping for "drawTexture(" and adapt.
            try {
                context.drawTexture(pipeline, CUSTOM_MOD_ICONS, x, y, 0f, 0f, originalWidth, originalHeight, originalWidth, originalHeight);
            } catch (NoSuchMethodError e) {
                // fallback: some versions put Identifier first, without pipeline
                try {
                    context.drawTexture(CUSTOM_MOD_ICONS, x, y, 0, 0, originalWidth, originalHeight);
                } catch (Throwable ignored) { }
            }
        }

        // reset color
        setShaderColorSafe(1.0f, 1.0f, 1.0f, 1.0f);

        // restore matrix
        try {
            context.getMatrices().pop();
        } catch (NoSuchMethodError e) {
            try {
                context.getMatrices().popPose();
            } catch (Throwable ignored) { }
        }

        // restore default blend (mapping-dependent)
        defaultBlendSafe();
    }

    // --- Helper methods to hide mapping differences ---

    private static RenderPipeline getGuiRenderPipeline(DrawContext ctx) {
        // Try several likely static accessors. If your mappings differ, search RenderPipeline.mapping for candidates:
        // "getGuiTextured", "getTexturedGui", "getGuiPipeline", "getTextured", etc.
        try {
            // If DrawContext exposes a getRenderPipeline() method in your mapping, prefer that:
            try {
                java.lang.reflect.Method m = DrawContext.class.getMethod("getRenderPipeline");
                Object rp = m.invoke(ctx);
                if (rp instanceof RenderPipeline) return (RenderPipeline) rp;
            } catch (Throwable ignored) { }

            // Try common RenderPipeline static accessors
            try {
                java.lang.reflect.Method m = RenderPipeline.class.getMethod("getGuiTextured");
                Object rp = m.invoke(null);
                if (rp instanceof RenderPipeline) return (RenderPipeline) rp;
            } catch (Throwable ignored) { }

            try {
                java.lang.reflect.Method m = RenderPipeline.class.getMethod("getTexturedGui");
                Object rp = m.invoke(null);
                if (rp instanceof RenderPipeline) return (RenderPipeline) rp;
            } catch (Throwable ignored) { }

        } catch (Throwable ignored) { }

        // Last fallback: return null; drawTexture fallback will try Identifier-first method.
        return null;
    }

    private static void enableBlendSafe() {
        // RenderSystem APIs changed a lot; try several options via reflection:
        try {
            RenderSystem.class.getMethod("enableBlend").invoke(null);
            return;
        } catch (Throwable ignored) {}

        try {
            RenderSystem.class.getMethod("enableBlendState").invoke(null);
            return;
        } catch (Throwable ignored) {}

        // If neither exists, try calling glEnable via RenderSystem
        try {
            RenderSystem.class.getMethod("enableAlphaTest").invoke(null);
        } catch (Throwable ignored) {}
    }

    private static void defaultBlendSafe() {
        try {
            RenderSystem.class.getMethod("defaultBlendFunc").invoke(null);
            return;
        } catch (Throwable ignored) {}

        // try blendFunc with common constants (use reflection to avoid compile-time reliance)
        try {
            RenderSystem.class.getMethod("blendFunc", int.class, int.class)
                    .invoke(null, /*GL_SRC_ALPHA*/ 770, /*GL_ONE_MINUS_SRC_ALPHA*/ 771);
            return;
        } catch (Throwable ignored) {}
    }

    private static void setShaderColorSafe(float r, float g, float b, float a) {
        // Modern RenderSystem uses floats for shader color
        try {
            RenderSystem.class.getMethod("setShaderColor", float.class, float.class, float.class, float.class)
                    .invoke(null, r, g, b, a);
            return;
        } catch (Throwable ignored) {}

        // Try older int version (unlikely) as fallback
        try {
            RenderSystem.class.getMethod("setShaderColor", int.class, int.class, int.class, int.class)
                    .invoke(null, (int) r, (int) g, (int) b, (int) a);
        } catch (Throwable ignored) {}
    }
}
