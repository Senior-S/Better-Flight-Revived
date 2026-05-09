package com.rejahtavi.betterflight.fabric.compat;

import com.beansgalaxy.backpacks.access.BackData;
import com.rejahtavi.betterflight.util.WingProviders;
import net.minecraft.world.item.ItemStack;

public final class BeansCompat {
    private BeansCompat() {
    }

    public static void register() {
        WingProviders.register(player -> {
            if (player instanceof BackData backData) {
                for (ItemStack stack : backData.beans_Backpacks_3$getBody()) {
                    if (WingProviders.isFunctionalWing(stack)) {
                        return stack;
                    }
                }
            }
            return ItemStack.EMPTY;
        });
    }
}
