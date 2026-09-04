package com.FuFu.CabbageJellyPack.Screen;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.FuFu.CabbageJellyPack.Screen.StaticData.*;

public class SettingsIO {

    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("pcb_transparent_settings.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static SettingData data = new SettingData();
    public static final Logger LOGGER = LogUtils.getLogger();

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
                showAlphaControls = data.showAlphaControls;
                openIntelligent = data.openIntelligent;

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
        data.showAlphaControls = showAlphaControls;
        data.openIntelligent = openIntelligent;

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

    public static void saveConfigData() {
        data.allowSendMessage = AllowSendMessage;
        data.cabbageEaster = CabbageEaster;
        try {
            String json = GSON.toJson(data); // 转换为 JSON 字符串
            Files.writeString(CONFIG_PATH, json); // 写入文件
        } catch (IOException e) {
            System.err.println("保存配置文件失败: " + e.getMessage());
        }
    }

    public static void resetAllParameters() {
        bgMode = BackgroundMode.valueOf("NONE");
        showHudText = false;
        showAlphaControls = false;
        openIntelligent = false;
        showHudButton = true;

        AllowSendMessage = true;
        CabbageEaster = false;

        data.bgMode = bgMode.name();
        data.showHudText = false;
        data.showAlphaControls = false;
        data.openIntelligent = false;
        data.showHudButton = true;

        data.allowSendMessage = true;
        data.cabbageEaster = false;

        InventoryAlpha = 1.0f;
        GrayBgAlpha = 1.0f;

        hudButton_X = 5;
        hudButton_Y = 5;

        data.inventoryAlpha = InventoryAlpha;
        data.GrayBgAlpha = GrayBgAlpha;

        data.hudButton_X = hudButton_X;
        data.hudButton_Y = hudButton_Y;
    }
}