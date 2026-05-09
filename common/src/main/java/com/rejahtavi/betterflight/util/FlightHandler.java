package com.rejahtavi.betterflight.util;

import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import com.rejahtavi.betterflight.common.FlightActionType;
import com.rejahtavi.betterflight.network.FlightMessages;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class FlightHandler {
    private FlightHandler() {
    }

    public static void handleClassicTakeoff(Player player) {
        Vec3 upwards = new Vec3(0.0D, BetterFlightCommonConfig.TAKE_OFF_THRUST, 0.0D).scale(getCeilingFactor(player));
        player.startFallFlying();
        player.push(upwards.x, upwards.y, upwards.z);
        FlightMessages.sendToServer(FlightActionType.TAKEOFF);
    }

    public static void handleClassicFlap(Player player) {
        double ceilingFactor = getCeilingFactor(player);
        Vec3 upwards = new Vec3(0.0D, BetterFlightCommonConfig.CLASSIC_FLAP_THRUST, 0.0D).scale(ceilingFactor);
        Vec3 forwards = player.getDeltaMovement().normalize().scale(BetterFlightCommonConfig.CLASSIC_FLAP_THRUST * 0.25).scale(ceilingFactor);
        Vec3 impulse = forwards.add(upwards);
        player.push(impulse.x, impulse.y, impulse.z);
        FlightMessages.sendToServer(FlightActionType.FLAP);
    }

    public static void handleFlare(Player player) {
        Vec3 dragDirection = player.getDeltaMovement().normalize().reverse();
        double velocitySquared = player.getDeltaMovement().lengthSqr();
        Vec3 dragThrust = dragDirection.scale(velocitySquared * BetterFlightCommonConfig.FLARE_DRAG);
        double fallingSpeed = player.getDeltaMovement().y();
        player.push(dragThrust.x, fallingSpeed < 0 ? dragThrust.y - fallingSpeed * .10 : dragThrust.y, dragThrust.z);
    }

    public static void handleFlightStaminaExhaustion(Player player) {
        player.causeFoodExhaustion((float) BetterFlightCommonConfig.exhaustionPerChargePoint);
    }

    public static void handleModernFlap(Player player) {
        pushModern(player, 0.55D);
        FlightMessages.sendToServer(FlightActionType.FLAP);
    }

    public static void handleModernBoost(Player player) {
        pushModern(player, 1.0D);
        FlightMessages.sendToServer(FlightActionType.BOOST);
    }

    public static void handleFlightStop() {
        FlightMessages.sendToServer(FlightActionType.STOP);
    }

    private static void pushModern(Player player, double boostCoefficient) {
        double deltaCoefficient = 0.1D;
        Vec3 looking = player.getLookAngle();
        Vec3 delta = player.getDeltaMovement();
        Vec3 impulse = delta.add(
                        looking.x * boostCoefficient + (looking.x * deltaCoefficient - delta.x) * 1.5,
                        looking.y * boostCoefficient + (looking.y * deltaCoefficient - delta.y) * 1.5,
                        looking.z * boostCoefficient + (looking.z * deltaCoefficient - delta.z) * 1.5)
                .scale(getCeilingFactor(player))
                .add(getUpVector(player).scale(0.25));
        player.push(impulse.x, impulse.y, impulse.z);
    }

    private static double getCeilingFactor(Player player) {
        double altitude = player.getY();
        if (altitude < BetterFlightCommonConfig.softCeiling) {
            return 1.0D;
        }
        if (altitude > BetterFlightCommonConfig.hardCeiling) {
            return 0.0D;
        }
        return 1.0D - (altitude - BetterFlightCommonConfig.softCeiling) / BetterFlightCommonConfig.ceilingRange;
    }

    private static Vec3 getUpVector(Player player) {
        float yaw = player.getYRot() % 360;
        double rads = yaw * (Math.PI / 180);
        Vec3 left = new Vec3(Math.cos(rads), 0, Math.sin(rads));
        return player.getLookAngle().cross(left);
    }
}
