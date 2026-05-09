package com.rejahtavi.betterflight.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface WingProvider {
    ItemStack findWings(Player player);
}
