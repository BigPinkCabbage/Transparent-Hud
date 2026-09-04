package com.fufu.cabbagejellypack.Hud;

import com.fufu.cabbagejellypack.Screen.SettingsIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import static com.fufu.cabbagejellypack.Screen.StaticData.hudButton_X;
import static com.fufu.cabbagejellypack.Screen.StaticData.hudButton_Y;

public class DraggableButton extends Button {

    private double offsetX;
    private double offsetY;

    public DraggableButton(int x, int y, int w, int h, Component text) {
        super(x, y, w, h, text, btn -> {}, DEFAULT_NARRATION);
    }

    public void startDrag(double mouseX, double mouseY) {
        offsetX = mouseX - getX();
        offsetY = mouseY - getY();
    }

    public void stopDrag() {
        // 拖动结束，当前无需清理内部状态
    }

    public void dragTo(double mouseX, double mouseY) {

        int newX = (int) (mouseX - offsetX);
        int newY = (int) (mouseY - offsetY);

        Minecraft mc = Minecraft.getInstance();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        newX = Mth.clamp(newX, 0, screenWidth - getWidth() - 77);
        newY = Mth.clamp(newY, 2, screenHeight - getHeight() - 18);

        hudButton_X = newX;
        hudButton_Y = newY;

        setX(newX);
        setY(newY);

        SettingsIO.saveCabbageData();
    }
}