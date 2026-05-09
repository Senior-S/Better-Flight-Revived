package com.rejahtavi.betterflight.fabric;

import com.rejahtavi.betterflight.BetterFlightClient;
import net.fabricmc.api.ClientModInitializer;

public final class BetterFlightFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BetterFlightClient.init();
    }
}
