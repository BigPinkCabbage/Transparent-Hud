package com.fufu.cabbagejellypack.Event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;

import static com.fufu.cabbagejellypack.Screen.StaticData.InventoryAlpha;

public class TransparentHelper {
    public static void enableBlendedRendering(DrawContext context) {
        RenderSystem.enableBlend();
        context.setShaderColor(1.0F, 1.0F, 1.0F, InventoryAlpha);
    }

    public static void disableBlendedRendering(DrawContext context) {
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}