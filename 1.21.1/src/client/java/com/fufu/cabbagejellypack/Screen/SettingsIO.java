package com.fufu.cabbagejellypack.Screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.fufu.cabbagejellypack.Screen.StaticData.*;
import static com.mojang.text2speech.Narrator.LOGGER;

public class SettingsIO {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("pcb_transparent_settings.json");

    public static SettingData data = new SettingData();

    public static class SettingData {
        public float GrayBgAlpha;
        public float inventoryAlpha;

        public String bgMode = "NONE";
        public boolean showHudText;
        public boolean showAlphaControls;
        public boolean openIntelligent;
        public boolean allowSendMessage;
        public boolean cabbageEaster;

        public int hudButton_X = 5;
        public int hudButton_Y = 5;

        public boolean showHudButton = true;
    }

    // ✅ 读取配置文件
    public static void loadCabbageData() {

        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH); // 读取 JSON 字符串
                data = GSON.fromJson(json, SettingData.class); // 转换为对象
                GrayBgAlpha = data.GrayBgAlpha;
                InventoryAlpha = data.inventoryAlpha;
                showHudText = data.showHudText;

                bgMode = BackgroundMode.valueOf(data.bgMode);

                AllowSendMessage = data.allowSendMessage;
                CabbageEaster = data.cabbageEaster;

                hudButton_X = data.hudButton_X;
                hudButton_Y = data.hudButton_Y;
                showHudButton = data.showHudButton;

                LOGGER.info("Cabbage config loaded!");
            } catch (IOException e) {
                System.err.println("读取配置文件失败: " + e.getMessage());
            }
        }
    }

    // ✅ 保存配置文件
    public static void saveCabbageData() {
        data.GrayBgAlpha = GrayBgAlpha;
        data.inventoryAlpha = InventoryAlpha;
        data.showHudText = showHudText;

        data.bgMode = bgMode.name();

        data.cabbageEaster = CabbageEaster;
        data.allowSendMessage = AllowSendMessage;

        data.hudButton_X = hudButton_X;
        data.hudButton_Y = hudButton_Y;
        data.showHudButton = showHudButton;
        try {
            String json = GSON.toJson(data); // 转换为 JSON 字符串
            Files.writeString(CONFIG_PATH, json); // 写入文件
        } catch (IOException e) {
            System.err.println("保存配置文件失败: " + e.getMessage());
        }
    }

    public static void saveIntelligentData() {

        data.openIntelligent = openIntelligent;
        data.showAlphaControls = showAlphaControls;
        try {
            String json = GSON.toJson(data); // 转换为 JSON 字符串
            Files.writeString(CONFIG_PATH, json); // 写入文件
        } catch (IOException e) {
            System.err.println("保存配置文件失败: " + e.getMessage());
        }
    }
}