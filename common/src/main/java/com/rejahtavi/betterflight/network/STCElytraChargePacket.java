package com.rejahtavi.betterflight.network;

import com.rejahtavi.betterflight.util.InputHandler;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;

public final class STCElytraChargePacket extends BaseS2CMessage {
    private final int charge;

    public STCElytraChargePacket(int charge) {
        this.charge = charge;
    }

    public STCElytraChargePacket(RegistryFriendlyByteBuf buffer) {
        this(buffer.readInt());
    }

    @Override
    public MessageType getType() {
        return FlightMessages.SERVER_TO_CLIENT_CHARGE;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.charge);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        InputHandler.charge = charge;
    }
}
