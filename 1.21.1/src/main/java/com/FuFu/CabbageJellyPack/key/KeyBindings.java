package com.FuFu.CabbageJellyPack.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class KeyBindings {
    public static final String CATEGORY = "key.category.cabbagejellypack";
    public static final String OPEN_PINKBAG_SCREEN = "key.cabbagejellypack.open_pinkbag_screen";

    public static final KeyMapping OPEN_SETTINGS = new KeyMapping(
            OPEN_PINKBAG_SCREEN,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            CATEGORY
    );
}
