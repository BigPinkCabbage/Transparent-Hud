package com.FuFu.CabbageJellyPack.Screen;

public class StaticData {
    public static float InventoryAlpha = 1.0f;
    public static float GrayBgAlpha = 1.0f;

    public static BackgroundMode bgMode = BackgroundMode.NONE;

    public static boolean showHudText = false;
    public static boolean showAlphaControls = true;
    public static boolean openIntelligent = false;

    public static boolean AllowSendMessage = true;
    public static boolean CabbageEaster = false;

    public static int hudButton_X = 5;
    public static int hudButton_Y = 5;

    public static boolean showHudButton = true;

    public enum BackgroundMode {
        NONE,        // 无背景
        BLUR,        // 毛玻璃
        GRAY         // 原版泥土
    }
}
