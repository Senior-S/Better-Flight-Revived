package com.rejahtavi.betterflight.neoforge;

import com.rejahtavi.betterflight.BetterFlight;
import com.rejahtavi.betterflight.BetterFlightClient;
import com.rejahtavi.betterflight.neoforge.compat.BeansCompat;
import com.rejahtavi.betterflight.neoforge.compat.CuriosCompat;
import dev.architectury.platform.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(BetterFlight.MODID)
public final class BetterFlightNeoForge {
    public BetterFlightNeoForge() {
        BetterFlight.init();
        if (Platform.isModLoaded("curios")) {
            CuriosCompat.register();
        }
        if (Platform.isModLoaded("beansbackpacks")) {
            BeansCompat.register();
        }
        if (FMLEnvironment.dist == Dist.CLIENT) {
            BetterFlightClient.init();
        }
    }
}
