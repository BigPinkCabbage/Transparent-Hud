package com.fufu.cabbagejellypack;

import com.fufu.cabbagejellypack.Screen.MainSettingScreen;
import com.fufu.cabbagejellypack.Screen.SettingsIO;
import com.fufu.cabbagejellypack.key.KeyBindings;
import com.fufu.cabbagejellypack.Screen.MainSettingScreen.ConfigurationScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(value = CabbageJellyPack.MODID, dist = Dist.CLIENT)
public class CabbageJellyPack {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cabbagejellypack";

    private static final Logger LOGGER = LoggerFactory.getLogger(CabbageJellyPack.class);
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CabbageJellyPack(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (mc, parent) -> new ConfigurationScreen(parent)
        );

        // 1.21.1 起 @EventBusSubscriber.Bus.MOD 已弃用，模组总线事件改在构造器里用 IEventBus 注册
        modEventBus.addListener(ClientModEvents::init);
        modEventBus.addListener(ClientModEvents::onRegisterKeyMappings);
    }

    public static class ClientModEvents {
        public static void init(FMLClientSetupEvent event) {
            SettingsIO.loadCabbageData(); // ✅ 游戏启动时加载设置
        }

        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.OPEN_SETTINGS);
        }
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onWorldUnload(net.neoforged.neoforge.event.level.LevelEvent.Unload event) {
            if (event.getLevel().isClientSide()) {
                LOGGER.info("世界关闭，保存配置...");
                SettingsIO.saveCabbageData(); // ✅ 保存配置
            }
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (event.getKey() == KeyBindings.OPEN_SETTINGS.getKey().getValue() && event.getAction() == 1) {
                Minecraft mc = Minecraft.getInstance();
                // 锁定条件：只有在没有任何界面打开时才允许打开菜单，
                // 避免在聊天栏、物品栏等其它界面按 J 时冲突地弹出本模组菜单
                if (mc.screen == null) {
                    MainSettingScreen.open(); // 无界面时打开
                } else if (mc.screen instanceof MainSettingScreen) {
                    mc.setScreen(null); // 如果已经打开本模组菜单，关闭它
                    SettingsIO.saveCabbageData();
                }
                // 其它界面(聊天栏、物品栏等)按下 J 一律忽略
            }
        }
    }
}
