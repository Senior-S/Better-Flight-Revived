package com.rejahtavi.betterflight.util;

import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import com.rejahtavi.betterflight.common.FlightActionType;
import com.rejahtavi.betterflight.network.FlightMessages;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ServerFlightStamina {
    private static final Map<UUID, Integer> CHARGE = new HashMap<>();
    private static final Map<UUID, Long> LAST_RECHARGE_TICK = new HashMap<>();

    private ServerFlightStamina() {
    }

    public static void remove(ServerPlayer player) {
        UUID uuid = player.getUUID();
        CHARGE.remove(uuid);
        LAST_RECHARGE_TICK.remove(uuid);
    }

    public static void resetAndSync(ServerPlayer player) {
        CHARGE.put(player.getUUID(), BetterFlightCommonConfig.maxCharge);
        LAST_RECHARGE_TICK.put(player.getUUID(), player.level().getGameTime());
        FlightMessages.sendToPlayer(BetterFlightCommonConfig.maxCharge, player);
    }

    public static boolean trySpend(ServerPlayer player, FlightActionType action) {
        if (player.isCreative()) {
            sync(player, BetterFlightCommonConfig.maxCharge);
            return true;
        }

        int cost = action == FlightActionType.TAKEOFF ? BetterFlightCommonConfig.takeOffCost : BetterFlightCommonConfig.flapCost;
        int charge = charge(player);
        if (charge < cost) {
            sync(player, charge);
            return false;
        }

        int updatedCharge = Math.max(0, charge - cost);
        CHARGE.put(player.getUUID(), updatedCharge);
        LAST_RECHARGE_TICK.put(player.getUUID(), player.level().getGameTime());
        sync(player, updatedCharge);
        return true;
    }

    public static boolean tryRecharge(ServerPlayer player) {
        if (player.isCreative()) {
            sync(player, BetterFlightCommonConfig.maxCharge);
            return true;
        }

        int charge = charge(player);
        if (charge >= BetterFlightCommonConfig.maxCharge || player.getFoodData().getFoodLevel() <= BetterFlightCommonConfig.minFood) {
            sync(player, charge);
            return false;
        }

        long currentTick = player.level().getGameTime();
        long lastRechargeTick = LAST_RECHARGE_TICK.getOrDefault(player.getUUID(), currentTick);
        int threshold = player.onGround() ? BetterFlightCommonConfig.rechargeTicksOnGround : BetterFlightCommonConfig.rechargeTicksInAir;
        if (currentTick - lastRechargeTick < threshold) {
            return false;
        }

        int updatedCharge = Math.min(BetterFlightCommonConfig.maxCharge, charge + 1);
        CHARGE.put(player.getUUID(), updatedCharge);
        LAST_RECHARGE_TICK.put(player.getUUID(), currentTick);
        sync(player, updatedCharge);
        return true;
    }

    private static int charge(ServerPlayer player) {
        return CHARGE.computeIfAbsent(player.getUUID(), uuid -> BetterFlightCommonConfig.maxCharge);
    }

    private static void sync(ServerPlayer player, int charge) {
        FlightMessages.sendToPlayer(charge, player);
    }
}
