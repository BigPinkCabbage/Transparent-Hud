package com.fufu.cabbagejellypack.LoggedInText;

import com.fufu.cabbagejellypack.CabbageJellyPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import static com.fufu.cabbagejellypack.Screen.StaticData.AllowSendMessage;

//用静态方法处理程序（这是一个玩家进入游戏后给玩家发送消息的程序）
@EventBusSubscriber(modid = CabbageJellyPack.MODID, value = Dist.CLIENT)
public class PlayerLoggedInHandler {
    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        // 仅在开启"不止一次通知"(AllowSendMessage)时发送登录提示
        if (!AllowSendMessage) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player != null) {
            // 构建完整的消息组件
            MutableComponent message = Component.translatable("message.login.welcome")
                    .append("\n")
                    .append(Component.translatable("message.login.command_help"))
                    .append("\n")
                    .append(Component.translatable("message.login.enjoy"));

            // 在客户端显示消息
            player.displayClientMessage(message, false);
        }
    }
}