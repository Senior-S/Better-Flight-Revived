package com.rejahtavi.betterflight.client;

import com.rejahtavi.betterflight.client.gui.HudLocation;
import com.rejahtavi.betterflight.common.ConfigFile;

public final class ClientConfig {
    public static final int BORDER_FLASH_TICKS = 5;
    public static final float FLAP_SOUND_PITCH = 2.0f;

    public static HudLocation hudLocation = HudLocation.BAR_CENTER;
    public static double takeOffVolume = 1.0D;
    public static double flapVolume = 0.5D;
    public static boolean classicHudStyle = false;

    private ClientConfig() {
    }

    public static void bake() {
        ConfigFile config = new ConfigFile("betterflight-client.properties");
        config.load();
        takeOffVolume = config.readDouble("TakeOffVolume", takeOffVolume, 0.0D, 1.0D);
        flapVolume = config.readDouble("FlapVolume", flapVolume, 0.0D, 1.0D);
        hudLocation = config.readEnum("HudLocation", hudLocation, HudLocation.class);
        classicHudStyle = config.readBoolean("classicHud", classicHudStyle);
        config.save();
    }

    public static void saveHudLocation() {
        save();
    }

    public static void save() {
        ConfigFile config = new ConfigFile("betterflight-client.properties");
        config.load();
        config.put("TakeOffVolume", takeOffVolume);
        config.put("FlapVolume", flapVolume);
        config.put("HudLocation", hudLocation.name());
        config.put("classicHud", classicHudStyle);
        config.save();
    }
}
