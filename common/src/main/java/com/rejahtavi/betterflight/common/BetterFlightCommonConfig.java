package com.rejahtavi.betterflight.common;

import java.util.ArrayList;
import java.util.List;

public final class BetterFlightCommonConfig {
    public static final double TAKE_OFF_SPEED = 0.170D;
    public static final double TAKE_OFF_THRUST = 1.0D;
    public static final double CLASSIC_FLAP_THRUST = 0.65D;
    public static final double FLARE_DRAG = 0.08D;
    public static final int TAKE_OFF_JUMP_DELAY = 4;

    public static int maxCharge = 20;
    public static int takeOffCost = 3;
    public static int flapCost = 2;
    public static int rechargeTicksInAir = 80;
    public static int rechargeTicksOnGround = 10;
    public static int flareTicksPerChargePoint = 40;
    public static double exhaustionPerChargePoint = 4.0D;
    public static int minFood = 6;
    public static int cooldownTicks = 10;
    public static int softCeiling = 256;
    public static int hardCeiling = 400;
    public static int ceilingRange = hardCeiling - softCeiling;
    public static boolean classicMode = false;
    public static List<String> additionalWingItems = new ArrayList<>();

    private BetterFlightCommonConfig() {
    }

    public static void bake() {
        ConfigFile config = new ConfigFile("betterflight-common.properties");
        config.load();
        maxCharge = config.readInt("MaxCharge", maxCharge, 3, 255);
        takeOffCost = config.readInt("TakeOffCost", takeOffCost, 0, 255);
        flapCost = config.readInt("FlapCost", flapCost, 0, 255);
        rechargeTicksInAir = config.readInt("RechargeTicksInAir", rechargeTicksInAir, 5, 600);
        rechargeTicksOnGround = config.readInt("RechargeTicksOnGround", rechargeTicksOnGround, 5, 600);
        flareTicksPerChargePoint = config.readInt("FlareTicksPerChargePoint", flareTicksPerChargePoint, 5, 600);
        exhaustionPerChargePoint = config.readDouble("ExhaustionPerChargePoint", exhaustionPerChargePoint, 0.0D, 20.0D);
        minFood = config.readInt("MinFood", minFood, 0, 20);
        cooldownTicks = config.readInt("CooldownTicks", cooldownTicks, 5, 200);
        softCeiling = config.readInt("softCeiling", softCeiling, 0, 10000);
        hardCeiling = config.readInt("hardCeiling", hardCeiling, 0, 10000);
        classicMode = config.readBoolean("classicMode", classicMode);
        additionalWingItems = config.readList("AdditionalWingItems", additionalWingItems);
        if (softCeiling > hardCeiling) {
            softCeiling = hardCeiling;
            config.put("softCeiling", softCeiling);
        }
        ceilingRange = Math.max(1, hardCeiling - softCeiling);
        config.save();
    }

    public static void applySynced(
            int maxCharge,
            int takeOffCost,
            int flapCost,
            int rechargeTicksInAir,
            int rechargeTicksOnGround,
            int flareTicksPerChargePoint,
            double exhaustionPerChargePoint,
            int minFood,
            int cooldownTicks,
            int softCeiling,
            int hardCeiling,
            boolean classicMode,
            List<String> additionalWingItems
    ) {
        BetterFlightCommonConfig.maxCharge = Math.clamp(maxCharge, 3, 255);
        BetterFlightCommonConfig.takeOffCost = Math.clamp(takeOffCost, 0, 255);
        BetterFlightCommonConfig.flapCost = Math.clamp(flapCost, 0, 255);
        BetterFlightCommonConfig.rechargeTicksInAir = Math.clamp(rechargeTicksInAir, 5, 600);
        BetterFlightCommonConfig.rechargeTicksOnGround = Math.clamp(rechargeTicksOnGround, 5, 600);
        BetterFlightCommonConfig.flareTicksPerChargePoint = Math.clamp(flareTicksPerChargePoint, 5, 600);
        BetterFlightCommonConfig.exhaustionPerChargePoint = Math.clamp(exhaustionPerChargePoint, 0.0D, 20.0D);
        BetterFlightCommonConfig.minFood = Math.clamp(minFood, 0, 20);
        BetterFlightCommonConfig.cooldownTicks = Math.clamp(cooldownTicks, 5, 200);
        BetterFlightCommonConfig.softCeiling = Math.clamp(softCeiling, 0, 10000);
        BetterFlightCommonConfig.hardCeiling = Math.clamp(hardCeiling, 0, 10000);
        BetterFlightCommonConfig.classicMode = classicMode;
        BetterFlightCommonConfig.additionalWingItems = List.copyOf(additionalWingItems);
        if (BetterFlightCommonConfig.softCeiling > BetterFlightCommonConfig.hardCeiling) {
            BetterFlightCommonConfig.softCeiling = BetterFlightCommonConfig.hardCeiling;
        }
        BetterFlightCommonConfig.ceilingRange = Math.max(1, BetterFlightCommonConfig.hardCeiling - BetterFlightCommonConfig.softCeiling);
    }
}
