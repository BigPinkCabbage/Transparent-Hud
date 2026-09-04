package com.fufu.cabbagejellypack.Hud;

import com.fufu.cabbagejellypack.Screen.SettingsIO;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import static com.fufu.cabbagejellypack.Screen.StaticData.*;

public class TransparentInventoryHud {

    private static DraggableButton dragButton;
    private static ResetButton resetButton;
    private static SliderWidget uiSlider;
    private static SliderWidget bgSlider;

    private static DraggableButton currentDraggingButton;
    private static SliderWidget currentSlider;

    private static boolean initialized = false;

    /**
     * 供主类调用的初始化方法
     */
    public static void init() {
        if (initialized) return;

        // 创建控件
        dragButton = new DraggableButton(0, 0, 12, 12, Text.literal("☰"));

        resetButton = new ResetButton(0, 0, 12, 12, Text.literal("↺"));

        uiSlider = new AlphaSlider(
                0, 0, 75, 15,
                "cabbagejellypack.hud.inventory_alpha",
                () -> InventoryAlpha,
                v -> {
                    InventoryAlpha = (float) v;
                    SettingsIO.saveCabbageData();
                }
        );

        bgSlider = new AlphaSlider(
                0, 0, 75, 15,
                "cabbagejellypack.hud.bg_alpha",
                () -> GrayBgAlpha,
                v -> {
                    GrayBgAlpha = (float) v;
                    SettingsIO.saveCabbageData();
                }
        );

        // HUD 控件的绘制改由 TransparentInventoryHudMouseMixin 在容器界面
        // （HandledScreen.render）渲染完成后再调用 renderHud，保证显示在最上层。

        initialized = true;
    }

    // ==================== 渲染逻辑 ====================

    public static void renderHud(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        if (!showHudButton) return;

        // 同步滑块值
        if (currentSlider != uiSlider) {
            ((AlphaSlider) uiSlider).syncFromSource();
        }
        if (currentSlider != bgSlider) {
            ((AlphaSlider) bgSlider).syncFromSource();
        }

        int x = hudButton_X;
        int y = hudButton_Y;

        // 更新控件位置
        dragButton.setX(x);
        dragButton.setY(y);

        resetButton.setX(x);
        resetButton.setY(y + 16);

        uiSlider.setX(x + 14);
        uiSlider.setY(y - 2);

        bgSlider.setX(x + 14);
        bgSlider.setY(y + 15);

        // 物品是 3D 模型，DrawContext.drawItem 会把它 translate 到 Z=150；而 GUI 纹理层
        // （RenderLayer.GUI）使用 LEQUAL 深度测试且是反向深度（Z 越大越靠前），所以物品
        // 会压在 Z=0 的控件上面。这里把控件推到 Z=1000，保证显示在物品之上。
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 1000);

        dragButton.render(context, mouseX, mouseY, tickDelta);
        resetButton.render(context, mouseX, mouseY, tickDelta);
        uiSlider.render(context, mouseX, mouseY, tickDelta);
        bgSlider.render(context, mouseX, mouseY, tickDelta);

        context.getMatrices().pop();
    }

    // ==================== 鼠标事件处理 ====================

    private static boolean isNotOnContainerScreen() {
        return !(MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?>);
    }

    public static boolean onMouseClick(double mouseX, double mouseY, int button) {
        if (!showHudButton) return false;
        if (isNotOnContainerScreen()) return false;

        // 拖动按钮（左键）
        if (button == 0 && dragButton.isMouseOver(mouseX, mouseY)) {
            currentDraggingButton = dragButton;
            dragButton.startDrag(mouseX, mouseY);
            return true; // 取消事件传递
        }

        // 重置按钮
        if (resetButton.isMouseOver(mouseX, mouseY)) {
            if (resetButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        // UI 透明度滑块
        if (uiSlider.isMouseOver(mouseX, mouseY)) {
            currentSlider = uiSlider;
            uiSlider.mouseClicked(mouseX, mouseY, button);
            return true;
        }

        // 背景透明度滑块
        if (bgSlider.isMouseOver(mouseX, mouseY)) {
            currentSlider = bgSlider;
            bgSlider.mouseClicked(mouseX, mouseY, button);
            return true;
        }

        return false;
    }

    public static boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!showHudButton) return false;
        if (isNotOnContainerScreen()) return false;

        if (currentDraggingButton != null) {
            currentDraggingButton.dragTo(mouseX, mouseY);
            return true;
        }

        if (currentSlider != null) {
            currentSlider.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            return true;
        }

        return false;
    }

    public static boolean onMouseRelease(double mouseX, double mouseY, int button) {
        if (isNotOnContainerScreen()) return false;

        if (currentDraggingButton != null) {
            currentDraggingButton.stopDrag();
            currentDraggingButton = null;
            return true;
        }

        if (currentSlider != null) {
            currentSlider.mouseReleased(mouseX, mouseY, button);
            currentSlider = null;
            return true;
        }

        return false;
    }
}
