package walksy.crosshairaddons.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    // These methods exist in Yarn mappings for DrawContext
    @Shadow
    public abstract void drawTexture(Identifier texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight);

    @Shadow
    public abstract void drawTexture(Identifier texture, int x, int y, int u, int v, int width, int height);
}
