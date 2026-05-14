package com.rejahtavi.betterflight.neoforge;

import com.rejahtavi.betterflight.BetterFlight;
import com.rejahtavi.betterflight.BetterFlightClient;
import com.rejahtavi.betterflight.client.gui.BetterFlightConfigScreen;
import com.rejahtavi.betterflight.neoforge.compat.BeansCompat;
import com.rejahtavi.betterflight.neoforge.compat.CuriosCompat;
import dev.architectury.platform.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

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
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (client, parent) -> BetterFlightConfigScreen.create(parent)
            );
            BetterFlightClient.init();
        }
    }
}
