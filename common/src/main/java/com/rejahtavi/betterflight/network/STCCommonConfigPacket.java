package com.rejahtavi.betterflight.network;

import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.Arrays;
import java.util.List;

public final class STCCommonConfigPacket extends BaseS2CMessage {
    private final int maxCharge;
    private final int takeOffCost;
    private final int flapCost;
    private final int rechargeTicksInAir;
    private final int rechargeTicksOnGround;
    private final int flareTicksPerChargePoint;
    private final double exhaustionPerChargePoint;
    private final int minFood;
    private final int cooldownTicks;
    private final int softCeiling;
    private final int hardCeiling;
    private final boolean classicMode;
    private final List<String> additionalWingItems;

    public STCCommonConfigPacket() {
        this(
                BetterFlightCommonConfig.maxCharge,
                BetterFlightCommonConfig.takeOffCost,
                BetterFlightCommonConfig.flapCost,
                BetterFlightCommonConfig.rechargeTicksInAir,
                BetterFlightCommonConfig.rechargeTicksOnGround,
                BetterFlightCommonConfig.flareTicksPerChargePoint,
                BetterFlightCommonConfig.exhaustionPerChargePoint,
                BetterFlightCommonConfig.minFood,
                BetterFlightCommonConfig.cooldownTicks,
                BetterFlightCommonConfig.softCeiling,
                BetterFlightCommonConfig.hardCeiling,
                BetterFlightCommonConfig.classicMode,
                BetterFlightCommonConfig.additionalWingItems
        );
    }

    public STCCommonConfigPacket(RegistryFriendlyByteBuf buffer) {
        this(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readDouble(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readBoolean(),
                readItemList(buffer)
        );
    }

    private STCCommonConfigPacket(
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
        this.maxCharge = maxCharge;
        this.takeOffCost = takeOffCost;
        this.flapCost = flapCost;
        this.rechargeTicksInAir = rechargeTicksInAir;
        this.rechargeTicksOnGround = rechargeTicksOnGround;
        this.flareTicksPerChargePoint = flareTicksPerChargePoint;
        this.exhaustionPerChargePoint = exhaustionPerChargePoint;
        this.minFood = minFood;
        this.cooldownTicks = cooldownTicks;
        this.softCeiling = softCeiling;
        this.hardCeiling = hardCeiling;
        this.classicMode = classicMode;
        this.additionalWingItems = additionalWingItems;
    }

    @Override
    public MessageType getType() {
        return FlightMessages.SERVER_TO_CLIENT_CONFIG;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(maxCharge);
        buffer.writeInt(takeOffCost);
        buffer.writeInt(flapCost);
        buffer.writeInt(rechargeTicksInAir);
        buffer.writeInt(rechargeTicksOnGround);
        buffer.writeInt(flareTicksPerChargePoint);
        buffer.writeDouble(exhaustionPerChargePoint);
        buffer.writeInt(minFood);
        buffer.writeInt(cooldownTicks);
        buffer.writeInt(softCeiling);
        buffer.writeInt(hardCeiling);
        buffer.writeBoolean(classicMode);
        buffer.writeUtf(String.join(",", additionalWingItems));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        BetterFlightCommonConfig.applySynced(
                maxCharge,
                takeOffCost,
                flapCost,
                rechargeTicksInAir,
                rechargeTicksOnGround,
                flareTicksPerChargePoint,
                exhaustionPerChargePoint,
                minFood,
                cooldownTicks,
                softCeiling,
                hardCeiling,
                classicMode,
                additionalWingItems
        );
    }

    private static List<String> readItemList(RegistryFriendlyByteBuf buffer) {
        String raw = buffer.readUtf();
        if (raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(entry -> !entry.isEmpty())
                .toList();
    }
}
