package com.rejahtavi.betterflight.util;

import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public final class WingProviders {
    private static final List<WingProvider> PROVIDERS = new ArrayList<>();
    private static boolean defaultsRegistered = false;

    private WingProviders() {
    }

    public static void registerDefaults() {
        if (defaultsRegistered) {
            return;
        }
        defaultsRegistered = true;
        register(WingProviders::findChestWings);
    }

    public static void register(WingProvider provider) {
        PROVIDERS.add(provider);
    }

    public static ItemStack findWings(Player player) {
        for (WingProvider provider : PROVIDERS) {
            ItemStack stack = provider.findWings(player);
            if (isFunctionalWing(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isFunctionalWing(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.is(Items.ELYTRA)) {
            return ElytraItem.isFlyEnabled(stack);
        }
        if (isConfiguredWingItem(stack.getItem())) {
            return !stack.isDamageableItem() || stack.getDamageValue() < stack.getMaxDamage() - 1;
        }
        return false;
    }

    private static ItemStack findChestWings(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST);
    }

    private static boolean isConfiguredWingItem(Item item) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        if (itemId == null) {
            return false;
        }
        for (String configuredId : BetterFlightCommonConfig.additionalWingItems) {
            ResourceLocation parsed = ResourceLocation.tryParse(configuredId);
            if (itemId.equals(parsed)) {
                return true;
            }
        }
        return false;
    }
}
