package walksy.crosshairaddons.manager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.awt.*;

public class CrosshairRendererManager {
    public static final CrosshairRendererManager INSTANCE = new CrosshairRendererManager();
    private final MinecraftClient client = MinecraftClient.getInstance();

    // ✅ Fixed path: textures/gui/modicons.png
    private static final Identifier CUSTOM_MOD_ICONS =
            new Identifier("crosshairaddons", "textures/gui/modicons.png");

    private int hitMarkerDisplayTicks = 0;

    public void renderCrosshair(DrawContext context) {
        if (!ConfigManager.showInThirdPerson && !this.client.options.getPerspective().isFirstPerson()) return;
        this.renderCustomIcons(context);
    }

    public void onArrowHit() {
        this.hitMarkerDisplayTicks = ConfigManager.hitMarkerTime;
    }

    public void onTick() {
        if (hitMarkerDisplayTicks > 0) {
            hitMarkerDisplayTicks--;
        }
    }

    private void renderCustomIcons(DrawContext context) {
        context.getMatrices().push();

        if (ConfigManager.environmentBlend) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                RenderSystem.SourceFactor.ONE_MINUS_DST_COLOR.value,
                RenderSystem.DestFactor.ONE_MINUS_SRC_COLOR.value,
                RenderSystem.SourceFactor.ONE.value,
                RenderSystem.DestFactor.ZERO.value
            );
        }

        if ((ConfigManager.playerIndicator && this.client.targetedEntity instanceof PlayerEntity) ||
            (this.client.targetedEntity instanceof MobEntity && ConfigManager.mobIndicator)) {
            context.drawTexture(CUSTOM_MOD_ICONS,
                (context.getScaledWindowWidth() - 9) / 2,
                (context.getScaledWindowHeight() - 9) / 2,
                0, 0, 9, 9);
        }

        int offsetY = 0; // Offset for the elytra indicator

        // Elytra indicator
        if (ConfigManager.elytraIndicator && this.isWearingElytra()) {
            double scale = ConfigManager.elytraSize;
            int originalWidth = 14;
            int originalHeight = 14;

            int centerX = (context.getScaledWindowWidth() - originalWidth) / 2;
            int centerY = (context.getScaledWindowHeight() + 9 - originalHeight) / 2 + offsetY;

            context.getMatrices().push();
            context.getMatrices().translate(centerX + (originalWidth / 2f), centerY + (originalHeight / 2f) + 3.7, 0);
            context.getMatrices().scale((float) scale, (float) scale, 1.0F);
            context.getMatrices().translate(-(originalWidth / 2f), -(originalHeight / 2f) + 3.7, 0);

            Color o = Color.WHITE;
            RenderSystem.setShaderColor(o.getRed() / 255f, o.getGreen() / 255f, o.getBlue() / 255f, 1);
            context.drawTexture(CUSTOM_MOD_ICONS, 0, 0, 10, 0, originalWidth, originalHeight - 3);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            context.getMatrices().pop();
        }

        // Hitmarker
        if (ConfigManager.hitMarker && this.hitMarkerDisplayTicks > 0) {
            switch (ConfigManager.hitMarkerType) {
                case FADE -> this.drawDefaultHitmarker(context, true);
                case ANIMATION -> this.drawAnimationHitmarker(context);
                case DEFAULT -> this.drawDefaultHitmarker(context, false);
            }
        }

        if (ConfigManager.environmentBlend) {
            RenderSystem.defaultBlendFunc();
        }

        context.getMatrices().pop();
    }

    private void drawDefaultHitmarker(DrawContext context, boolean fade) {
        if (fade) {
            int fadeDuration = Math.max(2, ConfigManager.hitMarkerSpeed);
            float fadeProgress = Math.min(1.0f, (float) this.hitMarkerDisplayTicks / fadeDuration);
            float alpha = fadeProgress;
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        }

        context.drawTexture(CUSTOM_MOD_ICONS,
            (context.getScaledWindowWidth() - 10) / 2,
            (context.getScaledWindowHeight() - 11) / 2,
            24, 0, 10, 10);

        if (fade) {
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }

    /**
     * Animation frames:
     * First frame = 0
     * Second frame = 13
     * Third frame = 24
     */
    private void drawAnimationHitmarker(DrawContext context) {
        int[] frameV = {24, 13, 0};
        int numFrames = frameV.length;

        int frameDuration = Math.max((ConfigManager.hitMarkerSpeed / numFrames) * 2, 1);
        int totalDuration = frameDuration * numFrames;

        boolean animationCompleted = this.hitMarkerDisplayTicks >= totalDuration;
        int frameIndex = animationCompleted ? (numFrames - 1) :
                Math.min(this.hitMarkerDisplayTicks / frameDuration, numFrames - 1);

        context.drawTexture(CUSTOM_MOD_ICONS,
            (context.getScaledWindowWidth() - 10) / 2,
            (context.getScaledWindowHeight() - 11) / 2,
            24, frameV[frameIndex], 10, 10);
    }

    public boolean isWearingElytra() {
        if (this.client.player == null) return false;
        ItemStack itemStack = this.client.player.getEquippedStack(EquipmentSlot.CHEST);
        return itemStack.isOf(Items.ELYTRA);
    }
}
