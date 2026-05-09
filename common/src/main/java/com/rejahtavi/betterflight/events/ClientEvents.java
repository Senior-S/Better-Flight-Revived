package com.rejahtavi.betterflight.events;

import com.rejahtavi.betterflight.client.ClientData;
import com.rejahtavi.betterflight.client.Keybinding;
import com.rejahtavi.betterflight.client.gui.ClassicHudOverlay;
import com.rejahtavi.betterflight.client.gui.StaminaHudOverlay;
import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import com.rejahtavi.betterflight.util.ElytraData;
import com.rejahtavi.betterflight.util.InputHandler;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class ClientEvents {
    private static boolean registered = false;
    private static boolean wasFlapKeyDown = false;
    private static boolean wasToggleKeyDown = false;

    public static double elytraDurability = 0.5D;

    private ClientEvents() {
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        InputHandler.charge = BetterFlightCommonConfig.maxCharge;
        ClientTickEvent.CLIENT_PRE.register(ClientEvents::onClientTick);
        ClientGuiEvent.RENDER_HUD.register((graphics, deltaTracker) -> {
            ClassicHudOverlay.renderOverlay(graphics);
            StaminaHudOverlay.renderOverlay(graphics);
        });
    }

    private static void onClientTick(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }

        while (Keybinding.widgetPosKey.consumeClick()) {
            ClassicHudOverlay.cycleWidgetLocation();
        }

        updateWingStatus(player);
        if (player.onGround()) {
            ClientData.setOffGroundTicks(0);
        } else {
            ClientData.tickOffGround();
        }

        ClassicHudOverlay.borderTick();
        if (ClientData.getCooldown() > 0) {
            ClientData.subCooldown(1);
        }

        InputHandler.handleRecharge(player);
        InputHandler.tryFlare(player);

        if (Keybinding.flapKey.isDown() && !wasFlapKeyDown) {
            if (ClientData.getCooldown() <= 0 && ClientData.isFlightEnabled()) {
                if (BetterFlightCommonConfig.classicMode) {
                    InputHandler.classicFlight(player);
                } else {
                    InputHandler.modernFlight(player);
                }
            }
            wasFlapKeyDown = true;
        }
        if (!Keybinding.flapKey.isDown()) {
            wasFlapKeyDown = false;
        }

        if (Keybinding.toggleKey.isDown() && !wasToggleKeyDown) {
            ClientData.setFlightEnabled(!ClientData.isFlightEnabled());
            wasToggleKeyDown = true;
        }
        if (!Keybinding.toggleKey.isDown()) {
            wasToggleKeyDown = false;
        }
    }

    private static void updateWingStatus(LocalPlayer player) {
        ElytraData elytraStack = InputHandler.findWings(player);
        if (elytraStack != null && elytraStack.durabilityRemaining() > 1) {
            ClientData.setWingStatus(true);
            elytraDurability = elytraStack.durabilityPercent();
        } else {
            ClientData.setWingStatus(false);
        }
    }
}
