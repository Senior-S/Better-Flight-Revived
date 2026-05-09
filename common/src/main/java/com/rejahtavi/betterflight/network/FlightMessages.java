package com.rejahtavi.betterflight.network;

import com.rejahtavi.betterflight.BetterFlight;
import com.rejahtavi.betterflight.common.FlightActionType;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.minecraft.server.level.ServerPlayer;

public final class FlightMessages {
    private static final SimpleNetworkManager NETWORK = SimpleNetworkManager.create(BetterFlight.MODID);
    static MessageType CLIENT_TO_SERVER_FLIGHT_ACTION;
    static MessageType SERVER_TO_CLIENT_CHARGE;
    static MessageType SERVER_TO_CLIENT_CONFIG;

    private FlightMessages() {
    }

    public static void register() {
        CLIENT_TO_SERVER_FLIGHT_ACTION = NETWORK.registerC2S("flight_action", CTSFlightEffectsPacket::new);
        SERVER_TO_CLIENT_CONFIG = NETWORK.registerS2C("common_config", STCCommonConfigPacket::new);
        SERVER_TO_CLIENT_CHARGE = NETWORK.registerS2C("elytra_charge", STCElytraChargePacket::new);
    }

    public static void sendToServer(FlightActionType action) {
        new CTSFlightEffectsPacket(action).sendToServer();
    }

    public static void sendToPlayer(int stamina, ServerPlayer player) {
        new STCElytraChargePacket(stamina).sendTo(player);
    }

    public static void sendConfigToPlayer(ServerPlayer player) {
        new STCCommonConfigPacket().sendTo(player);
    }
}
