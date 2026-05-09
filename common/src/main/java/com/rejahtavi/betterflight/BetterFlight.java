package com.rejahtavi.betterflight;

import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import com.rejahtavi.betterflight.common.Sounds;
import com.rejahtavi.betterflight.events.CommonEvents;
import com.rejahtavi.betterflight.network.FlightMessages;
import com.rejahtavi.betterflight.util.WingProviders;

public final class BetterFlight {
    public static final String MODID = "betterflight";
    public static final String MODNAME = "Better Flight";
    public static final String VERSION = "1.0.0";

    private BetterFlight() {
    }

    public static void init() {
        BetterFlightCommonConfig.bake();
        WingProviders.registerDefaults();
        Sounds.register();
        FlightMessages.register();
        CommonEvents.register();
    }
}
