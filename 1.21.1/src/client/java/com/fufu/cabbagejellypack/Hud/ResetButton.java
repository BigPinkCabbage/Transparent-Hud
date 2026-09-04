package com.fufu.cabbagejellypack.Hud;

import com.fufu.cabbagejellypack.Screen.SettingsIO;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;

import static com.fufu.cabbagejellypack.Screen.StaticData.hudButton_X;
import static com.fufu.cabbagejellypack.Screen.StaticData.hudButton_Y;

public class ResetButton extends ButtonWidget {

    public ResetButton(int x, int y, int w, int h, MutableText text) {
        super(x, y, w, h, text, btn -> {
            // 左键
            hudButton_X = 5;
            hudButton_Y = 5;
            SettingsIO.saveCabbageData();
        }, DEFAULT_NARRATION_SUPPLIER);
    }
}