package com.fufu.cabbagejellypack.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeGroupButtonWidget;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.fufu.cabbagejellypack.Event.TransparentHelper.*;
import static com.fufu.cabbagejellypack.Screen.StaticData.GrayBgAlpha;


@Mixin(InGameHud.class)
public class TransparentGuiMixin {
    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 0
            )
    )
    private void beforeHotbarBackground(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci
    ) {
        enableBlendedRendering(context);
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void afterHotbarBackground(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci
    ) {
        disableBlendedRendering(context);

    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 2
            )
    )
    private void beforeHotbarLeftHand(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci
    ) {
        enableBlendedRendering(context);
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 2,
                    shift = At.Shift.AFTER
            )
    )
    private void afterHotbarLeftHand(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci
    ) {
        disableBlendedRendering(context);
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 3
            )
    )
    private void beforeHotbarRightHand(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci
    ) {
        enableBlendedRendering(context);
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    private void afterHotbarRightHand(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci
    ) {
        disableBlendedRendering(context);
    }

    @Environment(EnvType.CLIENT)
    @Mixin(BeaconScreen.class)
    public static class TransparentBeaconScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(BrewingStandScreen.class)
    public static class TransparentBrewingStandScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(CartographyTableScreen.class)
    public static class TransparentCartographyTableScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(GenericContainerScreen.class)
    public static class TransparentChestScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeFirstDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterFirstDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 1
                )
        )
        private void beforeSecondDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 1,
                        shift = At.Shift.AFTER
                )
        )
        private void afterSecondDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(CrafterScreen.class)
    public static class TransparentCrafterScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawBackground(
                DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawBackground(
                DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(CraftingScreen.class)
    public static class TransparentCraftingScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawBackground(
                DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawBackground(
                DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(CreativeInventoryScreen.class)
    public abstract static class TransparentCreativeInventoryScreenMixin {

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0  // 如果有多个drawTexture调用，用ordinal指定第一个
                )
        )
        private void beforeSpecificDrawTexture(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
            enableBlendedRendering(context); // 30%透明度
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterSpecificDrawTexture(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
            disableBlendedRendering(context);
        }

        @Inject(
                method = "renderTabIcon",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"
                )
        )
        private void beforeDrawTabBackground(DrawContext context, ItemGroup group, CallbackInfo ci)
        {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "renderTabIcon",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTabBackground(DrawContext context, ItemGroup group, CallbackInfo ci)
        {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(EnchantmentScreen.class)
    public static class TransparentEnchantmentScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(ForgingScreen.class)
    public static class TransparentForgingScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(AbstractFurnaceScreen.class)
    public static class TransparentFurnaceScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(Generic3x3ContainerScreen.class)
    public static class TransparentGeneric3x3ContainerScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(GrindstoneScreen.class)
    public static class TransparentGrindstoneScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(HopperScreen.class)
    public static class TransparentHopperScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(HorseScreen.class)
    public static class TransparentHorseScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(LoomScreen.class)
    public static class TransparentLoomScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(MerchantScreen.class)
    public static class TransparentMerchantScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIFFIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIFFIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(RecipeGroupButtonWidget.class)

    public static class TransparentRecipeGroupButtonWidgetMixin {
        @Inject(
                method = "renderWidget",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                int mouseX,
                int mouseY,
                float delta,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "renderWidget",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                int mouseX,
                int mouseY,
                float delta,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(RecipeBookWidget.class)

    public static class TransparentRecipeBookWidgetMixin {
        @Inject(
                method = "render",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                int mouseX,
                int mouseY,
                float delta,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "render",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                int mouseX,
                int mouseY,
                float delta,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(Screen.class)

    public static class TransparentScreenMixin {
        @Inject(
                method = "renderInGameBackground",
                at = @At("HEAD")
        )
        private void onRenderInGameBackground(DrawContext context, CallbackInfo ci) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, GrayBgAlpha);

        }

        @Inject(
                method = "renderInGameBackground",
                at = @At("TAIL")
        )
        private void afterRenderInGameBackground(DrawContext context, CallbackInfo ci) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(ShulkerBoxScreen.class)
    public static class TransparentShulkerBoxScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(StonecutterScreen.class)
    public static class TransparentStonecutterScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0
                )
        )
        private void beforeDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        ordinal = 0,
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawTexture(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(InventoryScreen.class)
    public abstract static class TransparentSurviveInventoryScreenMixin {
        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"
                )
        )
        private void beforeDrawBackground(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            enableBlendedRendering(context);
        }

        @Inject(
                method = "drawBackground",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                        shift = At.Shift.AFTER
                )
        )
        private void afterDrawBackground(
                DrawContext context,
                float delta,
                int mouseX,
                int mouseY,
                CallbackInfo ci
        ) {
            disableBlendedRendering(context);
        }
    }
}
