package com.fufu.cabbagejellypack.Event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import static com.fufu.cabbagejellypack.Screen.StaticData.*;

public class TransparentHelper {
    public static void blitHudTransparentTexture(
            GuiGraphics graphics,
            ResourceLocation texture,
            int x, int y,
            int u, int v,
            int width, int height
    ) {
        if (texture == null) return;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.setColor(1.0F, 1.0F, 1.0F, InventoryAlpha); // 设置透明度
        graphics.blit(texture, x, y, u, v, width, height);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); // 恢复
        RenderSystem.disableBlend();
    }

    public static void blitHudTransparentTexture(
            GuiGraphics graphics,
            ResourceLocation texture,
            int x, int y, int b,
            float u, float v,
            int width, int height,
            int textureWidth, int textureHeight
    ) {
        if (texture == null) return;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.setColor(1.0F, 1.0F, 1.0F, InventoryAlpha); // 设置透明度
        graphics.blit(texture, x, y, b, u, v, width, height, textureWidth, textureHeight);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); // 恢复
        RenderSystem.disableBlend();
    }

    public static void blitSpriteHudTransparentTexture(
            GuiGraphics graphics,
            ResourceLocation sprite,
            int x, int y,
            int width, int height
    ) {
        if (sprite == null) return;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.setColor(1.0F, 1.0F, 1.0F, InventoryAlpha); // 设置透明度
        graphics.blitSprite(sprite, x, y, width, height);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); // 恢复
        RenderSystem.disableBlend();
    }
}