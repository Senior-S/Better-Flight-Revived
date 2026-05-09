package com.rejahtavi.betterflight.neoforge.compat;

import com.rejahtavi.betterflight.util.WingProviders;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosCompat {
    private CuriosCompat() {
    }

    public static void register() {
        WingProviders.register(player -> CuriosApi.getCuriosInventory(player)
                .flatMap(handler -> handler.findFirstCurio(WingProviders::isFunctionalWing))
                .map(result -> result.stack())
                .orElse(ItemStack.EMPTY));
    }
}
