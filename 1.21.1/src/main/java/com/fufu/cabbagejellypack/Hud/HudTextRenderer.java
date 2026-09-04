package com.fufu.cabbagejellypack.Hud;

import com.fufu.cabbagejellypack.CabbageJellyPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import static com.fufu.cabbagejellypack.Screen.StaticData.InventoryAlpha;
import static com.fufu.cabbagejellypack.Screen.StaticData.showHudText;

@EventBusSubscriber(modid = CabbageJellyPack.MODID, value = Dist.CLIENT)
public class HudTextRenderer {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        HudTextRenderer.render(guiGraphics);
    }

    private static int cachedPing = 0;
    private static double cachedLocalMs = 0;
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        tickCounter++;

        if (tickCounter >= 40) { // 2秒刷新一次
            tickCounter = 0;

            if (mc.isSingleplayer()) {

                // 单机
                if (mc.getSingleplayerServer() != null) {
                    cachedLocalMs = mc.getSingleplayerServer()
                            .getAverageTickTimeNanos() / 1_000_000.0;
                }

            }  else {

                // 多人
                if (mc.getConnection() != null) {
                    var entry = mc.getConnection()
                            .getPlayerInfo(mc.player.getUUID());

                    if (entry != null) {
                        cachedPing = entry.getLatency();
                    }
                }
            }
        }
    }

    public static void render(GuiGraphics guiGraphics) {
        if (!showHudText) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        LocalPlayer player = mc.player;

        Component text;
        int color;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // 左上角坐标
        if (level != null && player != null && level.isClientSide) {
            int xi = (int) player.getX();
            int yi = (int) player.getY();
            int zi = (int) player.getZ();

            Component coords = Component.translatable("hud.cabbagemod.position", xi, yi, zi);
            guiGraphics.drawString(
                    mc.font,
                    coords,
                    5,
                    5,
                    0xFFFFFF,
                    true
            );

            Component transparency = Component.translatable("hud.cabbagemod.transparency", InventoryAlpha);
            guiGraphics.drawString(
                    mc.font,
                    transparency,
                    5,
                    18,
                    0xFFFFFF
            );

        }
        if (mc.isSingleplayer()) {

            text = Component.translatable(
                    "hud.cabbagemod.rt",
                    String.format("%.1f", cachedLocalMs)
            );

            color = getLocalColor(cachedLocalMs);

        } else {

            text = Component.translatable(
                    "hud.cabbagemod.ping",
                    cachedPing
            );

            color = getPingColor(cachedPing);
        }

        int width = mc.font.width(text);

        guiGraphics.drawString(
                mc.font,
                text,
                screenWidth - width - 5,
                screenHeight - 12,
                color,
                true
        );
    }

    // ==============================
    // 颜色判断
    // ==============================

    private static int getPingColor(int ping) {
        if (ping < 60) return 0x00FF00;
        if (ping < 100) return 0xFFFFFF;
        if (ping < 140) return 0xFFA500;
        return 0xFF5555;
    }

    private static int getLocalColor(double ms) {
        if (ms < 10) return 0x00FF00;
        if (ms < 20) return 0xFFFFFF;
        if (ms < 30) return 0xFFA500;
        return 0xFF5555;
    }
}

