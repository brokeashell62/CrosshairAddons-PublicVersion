package walksy.crosshairaddons.manager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import walksy.crosshairaddons.CrosshairAddons;

public class CrosshairRendererManager {

    private static final Identifier CUSTOM_MOD_ICONS = new Identifier("crosshairaddons", "modicons.png");
    private final MinecraftClient client = MinecraftClient.getInstance();

    public void renderCrosshair(MatrixStack matrixStack, float scale) {
        int originalWidth = 16;
        int originalHeight = 16;

        RenderSystem.pushMatrix();  // Use pushMatrix instead of getMatrices().pushPose()
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        matrixStack.translate(-(float) originalWidth / 2, -(float) originalHeight / 2 + 3.7f, 0);
        matrixStack.scale(scale, scale, 1.0F);

        client.getTextureManager().bindTexture(CUSTOM_MOD_ICONS);
        // Draw texture at the center with the correct parameters
        RenderSystem.drawTexture(CUSTOM_MOD_ICONS, 0, 0, originalWidth, originalHeight);

        RenderSystem.disableBlend();
        RenderSystem.popMatrix();  // Use popMatrix instead of getMatrices().popPose()
    }
}
