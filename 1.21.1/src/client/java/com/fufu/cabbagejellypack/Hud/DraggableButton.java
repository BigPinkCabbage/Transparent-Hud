package com.fufu.cabbagejellypack.Hud;

import com.fufu.cabbagejellypack.Screen.SettingsIO;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.MathHelper;

import static com.fufu.cabbagejellypack.Screen.StaticData.hudButton_X;
import static com.fufu.cabbagejellypack.Screen.StaticData.hudButton_Y;

public class DraggableButton extends ButtonWidget {

    private double offsetX;
    private double offsetY;

    public DraggableButton(int x, int y, int w, int h, MutableText text) {
        super(x, y, w, h, text, btn -> {}, DEFAULT_NARRATION_SUPPLIER);
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

        MinecraftClient client = MinecraftClient.getInstance();

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        newX = MathHelper.clamp(newX, 0, screenWidth - getWidth() - 77);
        newY = MathHelper.clamp(newY, 2, screenHeight - getHeight() - 18);

        hudButton_X = newX;
        hudButton_Y = newY;

        setX(newX);
        setY(newY);

        SettingsIO.saveCabbageData();
    }
}