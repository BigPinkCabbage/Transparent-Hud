package com.fufu.cabbagejellypack.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

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
