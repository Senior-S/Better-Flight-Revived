package com.rejahtavi.betterflight.events;

import com.rejahtavi.betterflight.network.FlightMessages;
import com.rejahtavi.betterflight.util.ServerFlightStamina;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CommonEvents {
    private static final Map<UUID, GameType> LAST_GAME_MODES = new HashMap<>();

    private CommonEvents() {
    }

    public static void register() {
        PlayerEvent.PLAYER_JOIN.register(CommonEvents::syncPlayer);
        PlayerEvent.PLAYER_QUIT.register(player -> {
            LAST_GAME_MODES.remove(player.getUUID());
            ServerFlightStamina.remove(player);
        });
        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd, removalReason) -> syncPlayer(player));
        TickEvent.SERVER_POST.register(server -> server.getPlayerList().getPlayers().forEach(CommonEvents::syncIfGameModeChanged));
    }

    private static void syncIfGameModeChanged(ServerPlayer player) {
        GameType gameType = player.gameMode.getGameModeForPlayer();
        GameType previous = LAST_GAME_MODES.put(player.getUUID(), gameType);
        if (previous != null && previous != gameType) {
            syncPlayer(player);
        }
    }

    private static void syncPlayer(ServerPlayer player) {
        LAST_GAME_MODES.put(player.getUUID(), player.gameMode.getGameModeForPlayer());
        FlightMessages.sendConfigToPlayer(player);
        ServerFlightStamina.resetAndSync(player);
    }
}
