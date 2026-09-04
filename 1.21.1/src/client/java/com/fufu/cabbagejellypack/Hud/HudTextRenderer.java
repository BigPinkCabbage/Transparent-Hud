package com.fufu.cabbagejellypack.Hud;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import static com.fufu.cabbagejellypack.Screen.StaticData.InventoryAlpha;
import static com.fufu.cabbagejellypack.Screen.StaticData.showHudText;

public class HudTextRenderer {

    private static int cachedPing = 0;
    private static double cachedLocalMs = 0;
    private static int tickCounter = 0;

    public static void register() {

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            if (!showHudText) return;

            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;

            if (player == null) return;

            int x = (int) player.getX();
            int y = (int) player.getY();
            int z = (int) player.getZ();

            Text coords = Text.translatable("hud.cabbagemod.position", x, y, z);

            drawContext.drawText(
                    client.textRenderer,
                    coords,
                    5, 5,          // 左上角
                    0xFFFFFF,      // 白色
                    true           // 阴影
            );

            Text transparency = Text.translatable("hud.cabbagemod.transparency", InventoryAlpha);
            drawContext.drawText(
                    client.textRenderer,
                    transparency,
                    5,
                    18,
                    0xFFFFFF,
                    true
            );

        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
        if (client.player == null) return;

        tickCounter++;

        if (tickCounter >= 40) { // 2秒
            tickCounter = 0;

            if (client.isInSingleplayer()) {

                // 单机：获取本地服务器 tick 时间
                if (client.getServer() != null) {
                    cachedLocalMs = client.getServer().getAverageTickTime();
                }

            } else {

                // 多人：获取 Ping
                if (client.getNetworkHandler() != null) {
                    var entry = client.getNetworkHandler()
                            .getPlayerListEntry(client.player.getUuid());

                    if (entry != null) {
                        cachedPing = entry.getLatency();
                    }
                }
            }
        }
        });

        // ===== HUD 渲染 =====
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        Text text;
        int color;

        if (client.isInSingleplayer()) {

            text = Text.translatable(
                    "hud.cabbagemod.rt",
                    String.format("%.1f", cachedLocalMs)
            );

            color = getLocalColor(cachedLocalMs);

        } else {

            text = Text.translatable(
                    "hud.cabbagemod.ping",
                    cachedPing
            );

            color = getPingColor(cachedPing);
        }

        int width = client.textRenderer.getWidth(text);

        if (showHudText) {
            drawContext.drawText(
                    client.textRenderer,
                    text,
                    screenWidth - width - 5,
                    screenHeight - 12,
                    color,
                    true
            );
        }

        });
    }

    private static int getPingColor(int ping) {
        if (ping < 60) return 0x00FF00;      // 绿色
        if (ping < 100) return 0xFFFFFF;      // 白色
        if (ping < 140) return 0xFFA500;      // 橙色
        return 0xFF5555;
    }

    private static int getLocalColor(double ms) {
        if (ms < 10) return 0x00FF00;      // 绿色
        if (ms < 20) return 0xFFFFFF;      // 白色
        if (ms < 30) return 0xFFA500;      // 橙色
        return 0xFF5555;                   // 接近掉TPS 红色
    }
}

