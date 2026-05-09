package com.rejahtavi.betterflight.fabric.compat;

import com.rejahtavi.betterflight.util.WingProviders;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.item.ItemStack;

public final class TrinketsCompat {
    private TrinketsCompat() {
    }

    public static void register() {
        WingProviders.register(player -> TrinketsApi.getTrinketComponent(player)
                .flatMap(component -> component.getEquipped(WingProviders::isFunctionalWing).stream()
                        .map(entry -> entry.getB())
                        .findFirst())
                .orElse(ItemStack.EMPTY));
    }
}
