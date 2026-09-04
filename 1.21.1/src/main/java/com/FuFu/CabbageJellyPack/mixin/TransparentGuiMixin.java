package com.FuFu.CabbageJellyPack.mixin;

import com.FuFu.CabbageJellyPack.Event.TransparentHelper;
import com.FuFu.CabbageJellyPack.Hud.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.FuFu.CabbageJellyPack.Screen.StaticData.GrayBgAlpha;

@Mixin(Gui.class)
public class TransparentGuiMixin {
    //添加文字hud
    @Shadow
    @Final
    private LayeredDraw layers;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectOverlay(Minecraft mc, CallbackInfo ci) {
        LayeredDraw draw = new LayeredDraw();
        draw.add((guiGraphics, partialTicks) -> {
            HudTextRenderer.render(guiGraphics); // 传入时间参数
        });
        layers.add(draw, () -> !mc.options.hideGui);
    }

    //快捷栏透明度
    @Redirect(
            method = "renderItemHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V",
                    ordinal = 0  // 第一个blitSprite调用，即HOTBAR_SPRITE
            )
    )
    private void redirectHotbarSprite(
            GuiGraphics guiGraphics,
            ResourceLocation sprite,
            int x, int y, int width, int height
    )
    {
        TransparentHelper.blitSpriteHudTransparentTexture(guiGraphics, sprite, x, y, width, height);
    }

    @Redirect(
            method = "renderItemHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V",
                    ordinal = 2
            )
    )
    private void redirectHotbarLeftHand(
            GuiGraphics guiGraphics,
            ResourceLocation sprite,
            int x, int y, int width, int height
    )
    {
        TransparentHelper.blitSpriteHudTransparentTexture(guiGraphics, sprite, x, y, width, height);
    }

    @Redirect(
            method = "renderItemHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V",
                    ordinal = 3
            )
    )
    private void redirectHotbarRightHand(
            GuiGraphics guiGraphics,
            ResourceLocation sprite,
            int x, int y, int width, int height
    )
    {
        TransparentHelper.blitSpriteHudTransparentTexture(guiGraphics, sprite, x, y, width, height);
    }

    //-----下面是内部类-----修改其他hud透明度-----
    @Mixin(BeaconScreen.class)
    public static class TransparentBeaconScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(BrewingStandScreen.class)
    public static class TransparentBrewingStandScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(CartographyTableScreen.class)
    public static class TransparentCartographyTableScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(ContainerScreen.class)
    public static class TransparentContainerScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                        ordinal = 0  // 第一个blit调用
                )
        )
        private void removeFirstBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }

        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                        ordinal = 1  // 第二个blit调用
                )
        )
        private void removeSecondBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(CrafterScreen.class)
    public static class TransparentCrafterScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(CraftingScreen.class)
    public static class TransparentCraftingScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(CreativeModeInventoryScreen.class)
    public static class TransparentCreativeInventoryScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }

        @Redirect(
                method = "renderTabButton",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"
                )
        )
        private void removeBackgroundBlitSprite(
                GuiGraphics guiGraphics,
                ResourceLocation sprite,
                int x, int y, int width, int height
        )
        {
            TransparentHelper.blitSpriteHudTransparentTexture(guiGraphics, sprite, x, y, width, height);
        }
    }

    @Mixin(DispenserScreen.class)
    public static class TransparentDispenserScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(EnchantmentScreen.class)
    public static class TransparentEnchantmentScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(GrindstoneScreen.class)
    public static class TransparentGrindstoneScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(HopperScreen.class)
    public static class TransparentHopperMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(ItemCombinerScreen.class)
    public static class TransparentItemCombinerScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(LoomScreen.class)
    public static class TransparentLoomScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(MerchantScreen.class)
    public static class TransparentMerchantScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int b,
                float u, float v,
                int width, int height,
                int textureWidth, int textureHeight
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, b, u, v, width, height, textureWidth, textureHeight);
        }
    }

    @Mixin(RecipeBookTabButton.class)
    public static class TransparentRecipeBookTabButtonMixin {
        @Redirect(
                method = "renderWidget",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"
                )
        )
        private void removeBackgroundBlitSprite(
                GuiGraphics guiGraphics,
                ResourceLocation sprite,
                int x, int y, int width, int height
        )
        {
            TransparentHelper.blitSpriteHudTransparentTexture(guiGraphics, sprite, x, y, width, height);
        }
    }

    @Mixin(RecipeBookComponent.class)
    public static class TransparentRecipeBookUIMixin {
        @Redirect(
                method = "render",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(ShulkerBoxScreen.class)
    public static class TransparentShulkerBoxScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(StonecutterScreen.class)
    public static class TransparentStonecutterScreenMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    @Mixin(InventoryScreen.class)
    public static class TransparentSurviveUIMixin {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    //两个抽象类
    @Mixin(AbstractFurnaceScreen.class)
    public static class TransparentAbstractFurnaceScreenMixin  {
        @Redirect(
                method = "renderBg",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
                )
        )
        private void removeBackgroundBlit(
                GuiGraphics graphics,
                ResourceLocation texture,
                int x, int y,
                int u, int v,
                int width, int height
        )
        {
            TransparentHelper.blitHudTransparentTexture(graphics, texture, x, y, u, v, width, height);
        }
    }

    //-----重绘灰色背景(带有透明度)-----
    @Mixin(AbstractContainerScreen.class)
    public static class TransparentAbstractContainerScreenMixin {
        @Redirect(
                method = "renderBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderTransparentBackground(Lnet/minecraft/client/gui/GuiGraphics;)V"
                )
        )
        private void removeTransparentBackground(
                AbstractContainerScreen<?> instance,
                GuiGraphics graphics
        ) {
            if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>){
                Minecraft mc = Minecraft.getInstance();
                int width = mc.getWindow().getGuiScaledWidth();
                int height = mc.getWindow().getGuiScaledHeight();
                float backgroundAlpha = GrayBgAlpha; // 可配置透明度
                int rgb = 0x101010;
                int alphaTop = (int)(192 * backgroundAlpha);
                int alphaBottom = (int)(208 * backgroundAlpha);
                int colorTop = (alphaTop << 24) | rgb;
                int colorBottom = (alphaBottom << 24) | rgb;
                graphics.fillGradient(
                        0, 0,
                        width, height,
                        colorTop,
                        colorBottom
                );
            }
        }
    }
}