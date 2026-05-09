package com.rejahtavi.betterflight.client;

public final class ClientData {
    private static boolean wingStatus = false;
    private static boolean flaring = false;
    private static int offGroundTicks = 0;
    private static boolean flightEnabled = true;
    private static int cooldown = 0;

    private ClientData() {
    }

    public static boolean isWearingFunctionalWings() {
        return wingStatus;
    }

    public static void setWingStatus(boolean wingStatus) {
        ClientData.wingStatus = wingStatus;
    }

    public static boolean isFlaring() {
        return flaring;
    }

    public static void setIsFlaring(boolean flaring) {
        ClientData.flaring = flaring;
    }

    public static int getOffGroundTicks() {
        return offGroundTicks;
    }

    public static void setOffGroundTicks(int offGroundTicks) {
        ClientData.offGroundTicks = offGroundTicks;
    }

    public static void tickOffGround() {
        ClientData.offGroundTicks++;
    }

    public static boolean isFlightEnabled() {
        return flightEnabled;
    }

    public static void setFlightEnabled(boolean flightEnabled) {
        ClientData.flightEnabled = flightEnabled;
    }

    public static int getCooldown() {
        return cooldown;
    }

    public static void setCooldown(int ticks) {
        ClientData.cooldown = ticks;
    }

    public static void subCooldown(int ticks) {
        ClientData.cooldown = Math.max(ClientData.cooldown - ticks, 0);
    }
}
