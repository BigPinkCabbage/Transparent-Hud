package com.FuFu.CabbageJellyPack.Hud;

import com.FuFu.CabbageJellyPack.Screen.SettingsIO;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import static com.FuFu.CabbageJellyPack.Screen.StaticData.*;

public class ResetButton extends Button {

    public ResetButton(int x, int y, int w, int h, Component text) {
        super(x, y, w, h, text, btn -> {
            // 左键
            hudButton_X = 5;
            hudButton_Y = 5;
            SettingsIO.saveCabbageData();
        }, DEFAULT_NARRATION);
    }
}