package com.rejahtavi.betterflight.client.gui;

import com.rejahtavi.betterflight.BetterFlight;
import com.rejahtavi.betterflight.client.ClientConfig;
import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public final class BetterFlightConfigScreen {
    private BetterFlightConfigScreen() {
    }

    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("betterflight.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("betterflight.config.category.client"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("betterflight.config.group.hud"))
                                .option(enumOption("hud_location", HudLocation.BAR_CENTER, HudLocation.class,
                                        () -> ClientConfig.hudLocation,
                                        value -> ClientConfig.hudLocation = value))
                                .option(booleanOption("classic_hud", false,
                                        () -> ClientConfig.classicHudStyle,
                                        value -> ClientConfig.classicHudStyle = value))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("betterflight.config.group.audio"))
                                .option(doubleOption("take_off_volume", 1.0D, 0.0D, 1.0D, 0.05D,
                                        () -> ClientConfig.takeOffVolume,
                                        value -> ClientConfig.takeOffVolume = value))
                                .option(doubleOption("flap_volume", 0.5D, 0.0D, 1.0D, 0.05D,
                                        () -> ClientConfig.flapVolume,
                                        value -> ClientConfig.flapVolume = value))
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("betterflight.config.category.flight"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("betterflight.config.group.stamina"))
                                .option(intOption("max_charge", 20, 3, 255, 1,
                                        () -> BetterFlightCommonConfig.maxCharge,
                                        value -> BetterFlightCommonConfig.maxCharge = value))
                                .option(intOption("take_off_cost", 3, 0, 255, 1,
                                        () -> BetterFlightCommonConfig.takeOffCost,
                                        value -> BetterFlightCommonConfig.takeOffCost = value))
                                .option(intOption("flap_cost", 2, 0, 255, 1,
                                        () -> BetterFlightCommonConfig.flapCost,
                                        value -> BetterFlightCommonConfig.flapCost = value))
                                .option(intOption("min_food", 6, 0, 20, 1,
                                        () -> BetterFlightCommonConfig.minFood,
                                        value -> BetterFlightCommonConfig.minFood = value))
                                .option(doubleOption("exhaustion_per_charge_point", 4.0D, 0.0D, 20.0D, 0.25D,
                                        () -> BetterFlightCommonConfig.exhaustionPerChargePoint,
                                        value -> BetterFlightCommonConfig.exhaustionPerChargePoint = value))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("betterflight.config.group.timing"))
                                .option(intOption("recharge_ticks_in_air", 80, 5, 600, 5,
                                        () -> BetterFlightCommonConfig.rechargeTicksInAir,
                                        value -> BetterFlightCommonConfig.rechargeTicksInAir = value))
                                .option(intOption("recharge_ticks_on_ground", 10, 5, 600, 5,
                                        () -> BetterFlightCommonConfig.rechargeTicksOnGround,
                                        value -> BetterFlightCommonConfig.rechargeTicksOnGround = value))
                                .option(intOption("flare_ticks_per_charge_point", 40, 5, 600, 5,
                                        () -> BetterFlightCommonConfig.flareTicksPerChargePoint,
                                        value -> BetterFlightCommonConfig.flareTicksPerChargePoint = value))
                                .option(intOption("cooldown_ticks", 10, 5, 200, 1,
                                        () -> BetterFlightCommonConfig.cooldownTicks,
                                        value -> BetterFlightCommonConfig.cooldownTicks = value))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("betterflight.config.group.behavior"))
                                .option(booleanOption("classic_mode", false,
                                        () -> BetterFlightCommonConfig.classicMode,
                                        value -> BetterFlightCommonConfig.classicMode = value))
                                .option(intOption("soft_ceiling", 256, 0, 10000, 16,
                                        () -> BetterFlightCommonConfig.softCeiling,
                                        value -> BetterFlightCommonConfig.softCeiling = Math.min(value, BetterFlightCommonConfig.hardCeiling)))
                                .option(intOption("hard_ceiling", 400, 0, 10000, 16,
                                        () -> BetterFlightCommonConfig.hardCeiling,
                                        value -> BetterFlightCommonConfig.hardCeiling = Math.max(value, BetterFlightCommonConfig.softCeiling)))
                                .option(stringOption("additional_wing_items", "",
                                        () -> String.join(",", BetterFlightCommonConfig.additionalWingItems),
                                        BetterFlightConfigScreen::setAdditionalWingItems))
                                .build())
                        .build())
                .save(BetterFlightConfigScreen::save)
                .build()
                .generateScreen(parent);
    }

    private static void save() {
        BetterFlightCommonConfig.ceilingRange = Math.max(1, BetterFlightCommonConfig.hardCeiling - BetterFlightCommonConfig.softCeiling);
        BetterFlightCommonConfig.save();
        ClientConfig.save();
    }

    private static void setAdditionalWingItems(String value) {
        BetterFlightCommonConfig.additionalWingItems = Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(entry -> !entry.isEmpty())
                .toList();
    }

    private static Option<Integer> intOption(String key, int fallback, int min, int max, int step, Getter<Integer> getter, Setter<Integer> setter) {
        return Option.<Integer>createBuilder()
                .name(Component.translatable("betterflight.config.option." + key))
                .description(OptionDescription.of(Component.translatable("betterflight.config.option." + key + ".description")))
                .binding(fallback, getter::get, setter::set)
                .controller(option -> IntegerSliderControllerBuilder.create(option).range(min, max).step(step))
                .build();
    }

    private static Option<Double> doubleOption(String key, double fallback, double min, double max, double step, Getter<Double> getter, Setter<Double> setter) {
        return Option.<Double>createBuilder()
                .name(Component.translatable("betterflight.config.option." + key))
                .description(OptionDescription.of(Component.translatable("betterflight.config.option." + key + ".description")))
                .binding(fallback, getter::get, setter::set)
                .controller(option -> DoubleSliderControllerBuilder.create(option).range(min, max).step(step))
                .build();
    }

    private static Option<Boolean> booleanOption(String key, boolean fallback, Getter<Boolean> getter, Setter<Boolean> setter) {
        return Option.<Boolean>createBuilder()
                .name(Component.translatable("betterflight.config.option." + key))
                .description(OptionDescription.of(Component.translatable("betterflight.config.option." + key + ".description")))
                .binding(fallback, getter::get, setter::set)
                .controller(BooleanControllerBuilder::create)
                .build();
    }

    private static Option<String> stringOption(String key, String fallback, Getter<String> getter, Setter<String> setter) {
        return Option.<String>createBuilder()
                .name(Component.translatable("betterflight.config.option." + key))
                .description(OptionDescription.of(Component.translatable("betterflight.config.option." + key + ".description")))
                .binding(fallback, getter::get, setter::set)
                .controller(StringControllerBuilder::create)
                .build();
    }

    private static <T extends Enum<T>> Option<T> enumOption(String key, T fallback, Class<T> type, Getter<T> getter, Setter<T> setter) {
        return Option.<T>createBuilder()
                .name(Component.translatable("betterflight.config.option." + key))
                .description(OptionDescription.of(Component.translatable("betterflight.config.option." + key + ".description")))
                .binding(fallback, getter::get, setter::set)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(type).valueFormatter(BetterFlightConfigScreen::enumName))
                .build();
    }

    private static Component enumName(Enum<?> value) {
        return Component.translatable("betterflight.config.enum." + value.name().toLowerCase());
    }

    private interface Getter<T> {
        T get();
    }

    private interface Setter<T> {
        void set(T value);
    }
}
