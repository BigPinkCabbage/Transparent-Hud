package com.FuFu.CabbageJellyPack.LoggedInText;

import com.FuFu.CabbageJellyPack.CabbageJellyPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.FuFu.CabbageJellyPack.Screen.StaticData.AllowSendMessage;

@Mod.EventBusSubscriber(modid = CabbageJellyPack.MODID, value = Dist.CLIENT)
public class PlayerLoggedInHandler {
    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        if (!AllowSendMessage) return;
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