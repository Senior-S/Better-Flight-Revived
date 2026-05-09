package com.rejahtavi.betterflight.network;

import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import com.rejahtavi.betterflight.common.FlightActionType;
import com.rejahtavi.betterflight.common.Sounds;
import com.rejahtavi.betterflight.util.FlightHandler;
import com.rejahtavi.betterflight.util.ServerFlightStamina;
import com.rejahtavi.betterflight.util.WingProviders;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class CTSFlightEffectsPacket extends BaseC2SMessage {
    private final FlightActionType flightUpdate;

    public CTSFlightEffectsPacket(FlightActionType flightUpdate) {
        this.flightUpdate = flightUpdate;
    }

    public CTSFlightEffectsPacket(RegistryFriendlyByteBuf buffer) {
        this(buffer.readEnum(FlightActionType.class));
    }

    @Override
    public MessageType getType() {
        return FlightMessages.CLIENT_TO_SERVER_FLIGHT_ACTION;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(this.flightUpdate);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        if (player == null) {
            return;
        }

        switch (flightUpdate) {
            case FLAP -> {
                if (canFlap(player) && ServerFlightStamina.trySpend(player, flightUpdate)) {
                    playSound(player, Sounds.FLAP.get(), 0.5F, 2F);
                }
            }
            case BOOST -> {
                if (canFlap(player) && ServerFlightStamina.trySpend(player, flightUpdate)) {
                    playSound(player, Sounds.BOOST.get(), 2F, 1F);
                }
            }
            case TAKEOFF -> {
                if (canTakeOff(player) && ServerFlightStamina.trySpend(player, flightUpdate)) {
                    player.startFallFlying();
                    playSound(player, Sounds.FLAP.get(), 1F, 2F);
                }
            }
            case STOP -> player.stopFallFlying();
            case RECHARGE -> {
                if (ServerFlightStamina.tryRecharge(player)) {
                    FlightHandler.handleFlightStaminaExhaustion(player);
                }
            }
        }
    }

    private static void playSound(Player player, SoundEvent sound, float volume, float pitch) {
        player.level().playSound(null, BlockPos.containing(player.position()), sound, SoundSource.PLAYERS, volume, pitch);
    }

    private static boolean canFlap(ServerPlayer player) {
        return !WingProviders.findWings(player).isEmpty() && !player.onGround() && player.isFallFlying();
    }

    private static boolean canTakeOff(ServerPlayer player) {
        return !WingProviders.findWings(player).isEmpty()
                && player.isSprinting()
                && !player.onGround()
                && !player.isFallFlying()
                && player.getDeltaMovement().length() > BetterFlightCommonConfig.TAKE_OFF_SPEED;
    }
}
