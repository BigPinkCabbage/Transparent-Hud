package com.fufu.cabbagejellypack.Event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import com.fufu.cabbagejellypack.Screen.StaticData.*;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import static com.fufu.cabbagejellypack.Screen.StaticData.*;

@EventBusSubscriber(modid = com.fufu.cabbagejellypack.CabbageJellyPack.MODID, value = net.neoforged.api.distmarker.Dist.CLIENT)

public class ClientBrightnessHandler {

    private static float lastAlpha = -1.0f;

    private static final float MIN_ALPHA = 0.03f;
    private static final float MAX_ALPHA = 0.30f;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!openIntelligent) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }

        ClientLevel level = mc.level;
        BlockPos pos = mc.player.blockPosition();

        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        float skyDarken = level.getSkyDarken(1.0f);

        float alpha = computeAlpha(skyLight, blockLight, skyDarken);

        if (Math.abs(alpha - lastAlpha) > 0.001f) {
            lastAlpha = alpha;
            InventoryAlpha = alpha;
        }
    }

    private static float computeAlpha(int skyLight, int blockLight, float skyDarken) {
        float skyBrightness = Mth.clamp(skyDarken, 0.0f, 1.0f);
        float blockBrightness = blockLight / 15.0f;

        float brightness;
        if (skyLight >= 14) {
            brightness = skyBrightness * 0.85f + blockBrightness * 0.15f;
        } else if (skyLight >= 8) {
            brightness = skyBrightness * 0.30f + blockBrightness * 0.70f;
        } else {
            brightness = blockBrightness;
        }

        brightness = Mth.clamp(brightness, 0.0f, 1.0f);

        float alpha = MIN_ALPHA + brightness * (MAX_ALPHA - MIN_ALPHA);
        alpha = Mth.clamp(alpha, MIN_ALPHA, MAX_ALPHA);
        alpha = Math.round(alpha * 100.0f) / 100.0f;
        return alpha;
    }
}