package com.rejahtavi.betterflight;

import com.rejahtavi.betterflight.client.ClientConfig;
import com.rejahtavi.betterflight.client.Keybinding;
import com.rejahtavi.betterflight.events.ClientEvents;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;

public final class BetterFlightClient {
    private BetterFlightClient() {
    }

    public static void init() {
        ClientConfig.bake();
        KeyMappingRegistry.register(Keybinding.toggleKey);
        KeyMappingRegistry.register(Keybinding.flapKey);
        KeyMappingRegistry.register(Keybinding.flareKey);
        KeyMappingRegistry.register(Keybinding.widgetPosKey);
        ClientEvents.register();
    }
}
