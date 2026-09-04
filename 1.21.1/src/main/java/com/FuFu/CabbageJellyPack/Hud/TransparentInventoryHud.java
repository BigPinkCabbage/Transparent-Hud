package com.FuFu.CabbageJellyPack.Hud;

import com.FuFu.CabbageJellyPack.Screen.SettingsIO;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.FuFu.CabbageJellyPack.CabbageJellyPack.MODID;
import static com.FuFu.CabbageJellyPack.Screen.StaticData.*;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TransparentInventoryHud {
    private static DraggableButton dragButton;
    private static ResetButton resetButton;
    private static AbstractSliderButton uiSlider;
    private static AbstractSliderButton bgSlider;

    private static DraggableButton currentDraggingButton;
    private static AbstractSliderButton currentSlider;

    private static void init() {
        if (dragButton != null) return;

        dragButton = new DraggableButton(0, 0, 12, 12, Component.literal("☰"));

        resetButton = new ResetButton(0, 0, 12, 12, Component.literal("↺"));

        resetButton.setTooltip(
                Tooltip.create(
                        Component.translatable(
                                "cabbagejellypack.hud.reset.tooltip"
                        )
                )
        );

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

    }

    // ⭐ 渲染（核心！！）
    @SubscribeEvent
    public static void onRender(net.minecraftforge.client.event.ScreenEvent.Render.Post event) {
        if (!showHudButton) return;
        if (!(event.getScreen() instanceof AbstractContainerScreen<?>)) return;

        init();

        if (currentSlider != uiSlider) {
            ((AlphaSlider) uiSlider).syncFromSource();
        }

        if (currentSlider != bgSlider) {
            ((AlphaSlider) bgSlider).syncFromSource();
        }

        GuiGraphics g = event.getGuiGraphics();

        int x = hudButton_X;
        int y = hudButton_Y;

        // ⭐ 每帧更新位置
        dragButton.setX(x);
        dragButton.setY(y);

        resetButton.setX(x);
        resetButton.setY(y + 16);

        uiSlider.setX(x + 14);
        uiSlider.setY(y - 2);

        bgSlider.setX(x + 14);
        bgSlider.setY(y + 15);

        // ⭐ 提高层级（关键）
        g.pose().pushPose();
        g.pose().translate(0, 0, 1000);

        dragButton.render(g, event.getMouseX(), event.getMouseY(), event.getPartialTick());
        resetButton.render(g, event.getMouseX(), event.getMouseY(), event.getPartialTick());
        uiSlider.render(g, event.getMouseX(), event.getMouseY(), event.getPartialTick());
        bgSlider.render(g, event.getMouseX(), event.getMouseY(), event.getPartialTick());

        g.pose().popPose();
    }

    // ⭐ 鼠标点击
    @SubscribeEvent
    public static void onMouseClick(net.minecraftforge.client.event.ScreenEvent.MouseButtonPressed.Pre event) {

        if (!showHudButton) return;
        if (!(event.getScreen() instanceof AbstractContainerScreen<?>)) return;

        init();

        double mx = event.getMouseX();
        double my = event.getMouseY();

        // 拖动按钮
        if (event.getButton() == 0 && dragButton.isMouseOver(mx, my)) {

            currentDraggingButton = dragButton;
            dragButton.startDrag(mx, my);

            event.setCanceled(true);
            return;
        }

        // 重置按钮
        if (resetButton.isMouseOver(mx, my)) {

            if (resetButton.mouseClicked(mx, my, event.getButton())) {
                event.setCanceled(true);
                return;
            }
        }

        // UI透明度
        if (uiSlider.isMouseOver(mx, my)) {

            currentSlider = uiSlider;

            uiSlider.mouseClicked(mx, my, event.getButton());

            event.setCanceled(true);
            return;
        }

        // 背景透明度
        if (bgSlider.isMouseOver(mx, my)) {

            currentSlider = bgSlider;

            bgSlider.mouseClicked(mx, my, event.getButton());

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseDrag(net.minecraftforge.client.event.ScreenEvent.MouseDragged event) {

        if (!showHudButton) return;
        if (!(event.getScreen() instanceof AbstractContainerScreen<?>)) return;

        init();

        double mx = event.getMouseX();
        double my = event.getMouseY();

        // 正在拖按钮
        if (currentDraggingButton != null) {

            currentDraggingButton.dragTo(mx, my);

            event.setCanceled(true);
            return;
        }

        // 正在拖Slider
        if (currentSlider != null) {

            currentSlider.mouseDragged(
                    mx,
                    my,
                    event.getMouseButton(),
                    event.getDragX(),
                    event.getDragY()
            );

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseRelease(net.minecraftforge.client.event.ScreenEvent.MouseButtonReleased event) {

        if (!(event.getScreen() instanceof AbstractContainerScreen<?>))
            return;

        init();

        double mx = event.getMouseX();
        double my = event.getMouseY();

        if (currentDraggingButton != null) {

            currentDraggingButton.stopDrag();
            currentDraggingButton = null;

            event.setCanceled(true);
        }

        if (currentSlider != null) {

            currentSlider.mouseReleased(mx, my, event.getButton());
            currentSlider = null;

            event.setCanceled(true);
        }
    }
}

