package com.rejahtavi.betterflight.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.rejahtavi.betterflight.BetterFlight;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class Keybinding {
    public static final String KEY_CATEGORY_BETTERFLIGHT = BetterFlight.MODID;
    public static final String KEY_TOGGLE_FLIGHT = "betterflight.keys.toggle";
    public static final String KEY_FLAP = "betterflight.keys.flap";
    public static final String KEY_FLARE = "betterflight.keys.flare";
    public static final String KEY_WIDGET_POS = "betterflight.keys.widget";

    public static final KeyMapping toggleKey = new KeyMapping(KEY_TOGGLE_FLIGHT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F8, KEY_CATEGORY_BETTERFLIGHT);
    public static final KeyMapping flapKey = new KeyMapping(KEY_FLAP, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, KEY_CATEGORY_BETTERFLIGHT);
    public static final KeyMapping flareKey = new KeyMapping(KEY_FLARE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY_BETTERFLIGHT);
    public static final KeyMapping widgetPosKey = new KeyMapping(KEY_WIDGET_POS, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F10, KEY_CATEGORY_BETTERFLIGHT);

    private Keybinding() {
    }
}
