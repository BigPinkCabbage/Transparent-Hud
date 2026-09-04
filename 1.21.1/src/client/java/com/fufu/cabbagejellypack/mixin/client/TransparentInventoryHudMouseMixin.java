package com.fufu.cabbagejellypack.mixin.client;

import com.fufu.cabbagejellypack.Hud.TransparentInventoryHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 拦截容器界面（HandledScreen）的鼠标事件，将叠加在其上的 HUD 控件
 * （拖动按钮 / 重置按钮 / 透明度滑块）的交互转发给 {@link TransparentInventoryHud}，
 * 并在被消费时取消原版事件传递。
 */
@Mixin(HandledScreen.class)
public class TransparentInventoryHudMouseMixin {

    @Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"), cancellable = true)
    private void cabbage$onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (TransparentInventoryHud.onMouseClick(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseDragged(DDIDD)Z", at = @At("HEAD"), cancellable = true)
    private void cabbage$onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        if (TransparentInventoryHud.onMouseDrag(mouseX, mouseY, button, deltaX, deltaY)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseReleased(DDI)Z", at = @At("HEAD"), cancellable = true)
    private void cabbage$onMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (TransparentInventoryHud.onMouseRelease(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }

    // 在容器界面（槽位、物品、前景文字等）全部绘制完成之后渲染 HUD 控件，
    // 这样控件不会被物品栏贴图遮挡。
    @Inject(method = "render", at = @At("TAIL"))
    private void cabbage$onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TransparentInventoryHud.renderHud(context, mouseX, mouseY, delta);
    }
}
