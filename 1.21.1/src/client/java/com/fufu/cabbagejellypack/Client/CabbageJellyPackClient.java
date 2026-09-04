package com.fufu.cabbagejellypack.Client;

import com.fufu.cabbagejellypack.Event.ClientBrightnessHandler;
import com.fufu.cabbagejellypack.Hud.HudTextRenderer;
import com.fufu.cabbagejellypack.Hud.TransparentInventoryHud;
import com.fufu.cabbagejellypack.LoggedInText.CabbageModHelp;
import com.fufu.cabbagejellypack.LoggedInText.PlayerLoggedInHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fufu.cabbagejellypack.Screen.SettingsIO.loadCabbageData;
import static com.fufu.cabbagejellypack.Screen.SettingsIO.saveCabbageData;

public class CabbageJellyPackClient implements ClientModInitializer {

    public static final String MOD_ID = "cabbagejellypack";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final String CATEGORY = "key.category.cabbagejellypack";
    public static final String OPEN_PINKBAG_SCREEN = "key.cabbagejellypack.open_pinkbag_screen";
	@Override
	public void onInitializeClient() {
        LOGGER.info("Hello world!");

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            loadCabbageData();
            CabbageModHelp.register();
            PlayerLoggedInHandler.register();
            HudTextRenderer.register();
            TransparentInventoryHud.init();
            ClientBrightnessHandler.register();
        });

        KeyBinding openScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(OPEN_PINKBAG_SCREEN, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, CATEGORY));
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            while (openScreen.wasPressed()) {
                // 仅在玩家不在任何界面(聊天栏/物品栏/其他 GUI)时打开，避免与其它界面冲突。
                // 无论是否放行都排空按键队列，防止界面里按下的 j 在关闭界面后被缓存误触发。
                if (client.player != null && client.currentScreen == null) {
                    client.setScreen(new com.fufu.cabbagejellypack.Screen.MainSettingScreen());
                }
            }
        });

        // 客户端退出时保存（等价于 NeoForge 的世界卸载）
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            LOGGER.info("世界关闭，保存配置...");
            saveCabbageData();
        });
	}
}