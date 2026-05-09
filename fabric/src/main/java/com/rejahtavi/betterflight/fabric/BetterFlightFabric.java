package com.rejahtavi.betterflight.fabric;

import com.rejahtavi.betterflight.BetterFlight;
import com.rejahtavi.betterflight.fabric.compat.BeansCompat;
import com.rejahtavi.betterflight.fabric.compat.TrinketsCompat;
import dev.architectury.platform.Platform;
import net.fabricmc.api.ModInitializer;

public final class BetterFlightFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterFlight.init();
        if (Platform.isModLoaded("trinkets")) {
            TrinketsCompat.register();
        }
        if (Platform.isModLoaded("beansbackpacks")) {
            BeansCompat.register();
        }
    }
}
