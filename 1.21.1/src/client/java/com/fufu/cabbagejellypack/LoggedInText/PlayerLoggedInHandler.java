package com.fufu.cabbagejellypack.LoggedInText;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static com.fufu.cabbagejellypack.Screen.StaticData.AllowSendMessage;

public class PlayerLoggedInHandler {

    private static boolean shown = false; // 防止重复显示

    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {

            // 丢到主线程，等 HUD 完全初始化
            client.execute(PlayerLoggedInHandler::showMessage);
        });
    }

    private static void showMessage() {
        // 仅在开启"不止一次通知"(AllowSendMessage)时发送登录提示
        if (!AllowSendMessage) {
            return;
        }

        if (shown) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;

        if (player == null) return;

        shown = true;

        MutableText message = Text.translatable("message.login.welcome")
                .append("\n")
                .append(Text.translatable("message.login.command_help"))
                .append("\n")
                .append(Text.translatable("message.login.enjoy"));
        player.sendMessage(message, false);
    }
}