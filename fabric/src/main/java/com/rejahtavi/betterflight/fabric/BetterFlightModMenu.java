package com.rejahtavi.betterflight.fabric;

import com.rejahtavi.betterflight.client.gui.BetterFlightConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class BetterFlightModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BetterFlightConfigScreen::create;
    }
}
