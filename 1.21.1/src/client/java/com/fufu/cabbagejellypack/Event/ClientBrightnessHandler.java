package com.fufu.cabbagejellypack.Event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;

import static com.fufu.cabbagejellypack.Screen.StaticData.InventoryAlpha;
import static com.fufu.cabbagejellypack.Screen.StaticData.openIntelligent;

public class ClientBrightnessHandler {

    private static float lastAlpha = -1.0f;

    private static final float MIN_ALPHA = 0.03f;
    private static final float MAX_ALPHA = 0.30f;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientBrightnessHandler::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        if (!openIntelligent) {
            return;
        }

        if (client.player == null || client.world == null) {
            return;
        }

        ClientWorld world = client.world;
        BlockPos pos = client.player.getBlockPos();

        int skyLight = world.getLightLevel(LightType.SKY, pos);
        int blockLight = world.getLightLevel(LightType.BLOCK, pos);
        float skyBrightness = world.getSkyBrightness(1.0f);

        float alpha = computeAlpha(skyLight, blockLight, skyBrightness);

        if (Math.abs(alpha - lastAlpha) > 0.001f) {
            lastAlpha = alpha;
            InventoryAlpha = alpha;
        }
    }

    private static float computeAlpha(int skyLight, int blockLight, float skyBrightness) {
        float skyFactor = MathHelper.clamp(skyBrightness, 0.0f, 1.0f);
        float blockBrightness = blockLight / 15.0f;

        float brightness;
        if (skyLight >= 14) {
            brightness = skyFactor * 0.85f + blockBrightness * 0.15f;
        } else if (skyLight >= 8) {
            brightness = skyFactor * 0.30f + blockBrightness * 0.70f;
        } else {
            brightness = blockBrightness;
        }

        brightness = MathHelper.clamp(brightness, 0.0f, 1.0f);

        float alpha = MIN_ALPHA + brightness * (MAX_ALPHA - MIN_ALPHA);
        alpha = MathHelper.clamp(alpha, MIN_ALPHA, MAX_ALPHA);
        alpha = Math.round(alpha * 100.0f) / 100.0f;
        return alpha;
    }
}
