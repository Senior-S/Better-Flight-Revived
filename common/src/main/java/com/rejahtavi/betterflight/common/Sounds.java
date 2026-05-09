package com.rejahtavi.betterflight.common;

import com.rejahtavi.betterflight.BetterFlight;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class Sounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BetterFlight.MODID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> FLAP = createEvent("betterflight.flap");
    public static final RegistrySupplier<SoundEvent> BOOST = createEvent("betterflight.boost");

    private Sounds() {
    }

    public static void register() {
        SOUNDS.register();
    }

    private static RegistrySupplier<SoundEvent> createEvent(String sound) {
        return SOUNDS.register(sound, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BetterFlight.MODID, sound)));
    }
}
