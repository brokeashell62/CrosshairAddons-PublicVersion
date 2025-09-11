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
    public static CrosshairRendererManager INSTANCE = new CrosshairRendererManager();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Identifier CUSTOM_MOD_ICONS = new Identifier("crosshairaddons", "modicons.png");
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
        context.getMatrices().pushPose();

        if (ConfigManager.environmentBlend) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                RenderSystem.SourceFactor.ONE_MINUS_DST_COLOR,
                RenderSystem.DestFactor.ONE_MINUS_SRC_COLOR,
                RenderSystem.SourceFactor.ONE,
                RenderSystem.DestFactor.ZERO
            );
        }

        if ((ConfigManager.playerIndicator && this.client.targetedEntity instanceof PlayerEntity) ||
            (this.client.targetedEntity instanceof MobEntity && ConfigManager.mobIndicator)) {
            context.drawTexture(this.CUSTOM_MOD_ICONS,
                (context.getScaledWindowWidth() - 9) / 2,
                (context.getScaledWindowHeight() - 9) / 2,
                0.0f, 0.0f, 9.0f, 9.0f, 0, 0, 1, 1);
        }

        int offsetY = 0; // Offset for the elytra indicator

        // Render elytra indicator
        if (ConfigManager.elytraIndicator && this.isWearingElytra()) {
            double scale = ConfigManager.elytraSize;
            int originalWidth = 14;
            int originalHeight = 14;

            int centerX = (context.getScaledWindowWidth() - originalWidth) / 2;
            int centerY = (context.getScaledWindowHeight() + 9 - originalHeight) / 2 + offsetY;

            context.getMatrices().pushPose();
            context.getMatrices().translate(centerX + (float) originalWidth / 2,
                                            centerY + (float) originalHeight / 2 + 3.7f, 0);
            context.getMatrices().scale((float) scale, (float) scale, 1.0F);
            context.getMatrices().translate(-(float) originalWidth / 2, -(float) originalHeight / 2 + 3.7f, 0);
            Color o = Color.WHITE;
            RenderSystem.setShaderColor(o.getRed() / 255f, o.getGreen() / 255f, o.getBlue() / 255f, 1);
            context.drawTexture(CUSTOM_MOD_ICONS, 0, 0, 10, 0, originalWidth, originalHeight - 3);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            context.getMatrices().popPose();
        }

        // Render hitmarker
        if (ConfigManager.hitMarker && this.hitMarkerDisplayTicks > 0) {
            switch (ConfigManager.hitMarkerType) {
                case FADE: {
                    this.drawDefaultHitmarker(context, true);
                    break;
                }

                case ANIMATION: {
                    this.drawAnimationHitmarker(context);
                    break;
                }

                case DEFAULT: {
                    this.drawDefaultHitmarker(context, false);
                    break;
                }
            }
        }

        if (ConfigManager.environmentBlend) {
            RenderSystem.defaultBlendFunc();
        }
        context.getMatrices().popPose();
    }

    private void drawDefaultHitmarker(DrawContext context, boolean fade) {
        if (fade) {
            int fadeDuration = Math.max(2, ConfigManager.hitMarkerSpeed);
            float fadeProgress = Math.min(1.0f, (float) this.hitMarkerDisplayTicks / fadeDuration);
            float alpha = 0 + fadeProgress;
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        }

        context.drawTexture(CUSTOM_MOD_ICONS,
            (context.getScaledWindowWidth() - 10) / 2,
            (context.getScaledWindowHeight() - 11) / 2,
            24.0f, 0.0f, 10.0f, 10.0f, 0, 0, 1, 1);

        if (fade) {
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }

    private void drawAnimationHitmarker(DrawContext context) {
        int[] frameV = {24, 13, 0};
        int numFrames = frameV.length;

        int frameDuration = Math.max((ConfigManager.hitMarkerSpeed / numFrames) * 2, 1);

        int totalDuration = frameDuration * numFrames;

        boolean animationCompleted = this.hitMarkerDisplayTicks >= totalDuration;
        int frameIndex = animationCompleted ? (numFrames - 1) : Math.min((this.hitMarkerDisplayTicks / frameDuration), numFrames - 1);

        context.drawTexture(CUSTOM_MOD_ICONS,
            (context.getScaledWindowWidth() - 10) / 2,
            (context.getScaledWindowHeight() - 11) / 2,
            24.0f, frameV[frameIndex], 10.0f, 10.0f, 0, 0, 1, 1);
    }

    public boolean isWearingElytra() {
        ItemStack itemStack = this.client.player.getEquippedStack(EquipmentSlot.CHEST);
        return itemStack.isOf(Items.ELYTRA);
    }
}
