package com.FuFu.CabbageJellyPack;

import com.FuFu.CabbageJellyPack.Screen.MainSettingScreen.ConfigurationScreen;
import com.FuFu.CabbageJellyPack.Screen.MainSettingScreen;
import com.FuFu.CabbageJellyPack.Screen.SettingsIO;
import com.FuFu.CabbageJellyPack.key.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CabbageJellyPack.MODID)
public class CabbageJellyPack {
    // Define mod id in a common place for everything to reference

    public static final String MODID = "cabbagejellypack";
    private static final Logger LOGGER = LoggerFactory.getLogger(CabbageJellyPack.class);
    public CabbageJellyPack(FMLJavaModLoadingContext context) {
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, parent) -> new ConfigurationScreen(parent)
                )
        );
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.OPEN_SETTINGS);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            SettingsIO.loadCabbageData(); // ✅ 游戏启动时加载设置
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onWorldUnload(net.minecraftforge.event.level.LevelEvent.Unload event) {
            if (event.getLevel().isClientSide()) {
                LOGGER.info("世界关闭，保存配置...");
                SettingsIO.saveCabbageData(); // ✅ 保存配置
            }
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (event.getKey() == KeyBindings.OPEN_SETTINGS.getKey().getValue() && event.getAction() == 1) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.screen instanceof MainSettingScreen) {
                    // 当前已打开本模组的菜单：按 J 关闭它并保存
                    mc.setScreen(null);
                    SettingsIO.saveCabbageData();
                } else if (mc.screen == null) {
                    // 当前没有任何界面（不在聊天栏/物品栏等其他屏幕）时才允许打开菜单
                    MainSettingScreen.open();
                }
                // 其它情况（聊天栏、物品栏、配置界面等正打开着）：直接忽略按键，避免与这些界面冲突
            }
        }
    }
}
