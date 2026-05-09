package com.rejahtavi.betterflight.client.gui;

import com.rejahtavi.betterflight.BetterFlight;
import com.rejahtavi.betterflight.client.ClientConfig;
import com.rejahtavi.betterflight.client.ClientData;
import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import com.rejahtavi.betterflight.events.ClientEvents;
import com.rejahtavi.betterflight.util.InputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public final class ClassicHudOverlay {
    private static final int OFFSET_FROM_CURSOR = 24;
    private static final int OFFSET_TO_HOTBAR_SIDES = 105;
    private static final int OFFSET_ABOVE_HOTBAR = 44;
    private static final int TEXTURE_SIZE = 128;
    private static final int ICON_SIZE = 16;
    private static final int HALF_ICON_SIZE = 8;
    private static final int SPRITE_DURABILITY_FULL = 0;
    private static final int SPRITE_DURABILITY_HALF = ICON_SIZE;
    private static final int SPRITE_DURABILITY_QUARTER = 2 * ICON_SIZE;
    private static final int SPRITE_DURABILITY_LOW = 3 * ICON_SIZE;
    private static final int SPRITE_BORDER_BLACK = 0;
    private static final int SPRITE_BORDER_RECHARGE = ICON_SIZE;
    private static final int SPRITE_BORDER_DEPLETION = 2 * ICON_SIZE;
    private static final int SPRITE_BORDER_FLARE = 3 * ICON_SIZE;
    private static final int SPRITE_METER_EMPTY = 4 * ICON_SIZE;
    private static final int SPRITE_METER_FULL = 5 * ICON_SIZE;
    private static final int SPRITE_ALARM = 6 * ICON_SIZE;

    private static final Random random = new Random(System.currentTimeMillis());
    private static final ResourceLocation ELYTRA_ICONS = ResourceLocation.fromNamespaceAndPath(BetterFlight.MODID, "textures/elytraicons.png");
    private static int rechargeBorderTimer = 0;
    private static int depletionBorderTimer = 0;

    private ClassicHudOverlay() {
    }

    public static void renderOverlay(GuiGraphics guiGraphics) {
        if (!ClientData.isFlightEnabled() || !ClientData.isWearingFunctionalWings() || !ClientConfig.classicHudStyle) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.isPassenger()) {
            return;
        }

        int shakeX = 0;
        int shakeY = 0;
        int scaleWidth = mc.getWindow().getGuiScaledWidth();
        int scaleHeight = mc.getWindow().getGuiScaledHeight();
        int widgetPosX;
        int widgetPosY;

        switch (ClientConfig.hudLocation) {
            case CURSOR_BELOW -> {
                widgetPosX = scaleWidth / 2 - HALF_ICON_SIZE;
                widgetPosY = scaleHeight / 2 + OFFSET_FROM_CURSOR - HALF_ICON_SIZE;
            }
            case CURSOR_LEFT -> {
                widgetPosX = scaleWidth / 2 - OFFSET_FROM_CURSOR - HALF_ICON_SIZE;
                widgetPosY = scaleHeight / 2 - HALF_ICON_SIZE;
            }
            case CURSOR_RIGHT -> {
                widgetPosX = scaleWidth / 2 + OFFSET_FROM_CURSOR - HALF_ICON_SIZE;
                widgetPosY = scaleHeight / 2 - HALF_ICON_SIZE;
            }
            case CURSOR_ABOVE -> {
                widgetPosX = scaleWidth / 2 - HALF_ICON_SIZE;
                widgetPosY = scaleHeight / 2 - OFFSET_FROM_CURSOR - HALF_ICON_SIZE;
            }
            case BAR_LEFT -> {
                widgetPosX = scaleWidth / 2 - OFFSET_TO_HOTBAR_SIDES - HALF_ICON_SIZE;
                widgetPosY = scaleHeight - 12 - HALF_ICON_SIZE;
            }
            case BAR_RIGHT -> {
                widgetPosX = scaleWidth / 2 + OFFSET_TO_HOTBAR_SIDES - HALF_ICON_SIZE;
                widgetPosY = scaleHeight - 12 - HALF_ICON_SIZE;
            }
            default -> {
                widgetPosX = scaleWidth / 2 - HALF_ICON_SIZE;
                widgetPosY = scaleHeight - OFFSET_ABOVE_HOTBAR - HALF_ICON_SIZE;
            }
        }

        int durabilityOffset = SPRITE_DURABILITY_FULL;
        if (ClientEvents.elytraDurability > 0.50f) durabilityOffset = SPRITE_DURABILITY_HALF;
        if (ClientEvents.elytraDurability > 0.75f) durabilityOffset = SPRITE_DURABILITY_QUARTER;
        if (ClientEvents.elytraDurability > 0.90f) durabilityOffset = SPRITE_DURABILITY_LOW;

        int borderOffset = SPRITE_BORDER_BLACK;
        if (ClientEvents.elytraDurability > 0.95 && mc.level != null) {
            long thisTick = mc.level.getGameTime();
            borderOffset = (int) (((thisTick / 5) % 2) * ICON_SIZE) + SPRITE_ALARM;
            if (((thisTick / 3) % 2) > 0) {
                shakeX = random.nextInt(3) - 1;
            } else {
                shakeY = random.nextInt(3) - 1;
            }
        } else if (ClientData.isFlaring()) {
            borderOffset = SPRITE_BORDER_FLARE;
        } else if (depletionBorderTimer > 0) {
            borderOffset = SPRITE_BORDER_DEPLETION;
        } else if (rechargeBorderTimer > 0) {
            borderOffset = SPRITE_BORDER_RECHARGE;
        }

        int drainedPixels = (int) Math.floor((1.0f - (float) InputHandler.charge / (float) BetterFlightCommonConfig.maxCharge) * ICON_SIZE);
        guiGraphics.blit(ELYTRA_ICONS, widgetPosX + shakeX, widgetPosY + shakeY, SPRITE_METER_FULL, durabilityOffset, ICON_SIZE, ICON_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
        guiGraphics.blit(ELYTRA_ICONS, widgetPosX + shakeX, widgetPosY + shakeY, SPRITE_METER_EMPTY, durabilityOffset, ICON_SIZE, drainedPixels, TEXTURE_SIZE, TEXTURE_SIZE);
        guiGraphics.blit(ELYTRA_ICONS, widgetPosX + shakeX, widgetPosY + shakeY, borderOffset, durabilityOffset, ICON_SIZE, ICON_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    public static void borderTick() {
        if (depletionBorderTimer > 0) depletionBorderTimer--;
        if (rechargeBorderTimer > 0) rechargeBorderTimer--;
    }

    public static void setDepletionBorderTimer(int ticks) {
        depletionBorderTimer = ticks;
    }

    public static void setRechargeBorderTimer(int ticks) {
        rechargeBorderTimer = ticks;
    }

    public static void cycleWidgetLocation() {
        switch (ClientConfig.hudLocation) {
            case BAR_CENTER -> ClientConfig.hudLocation = HudLocation.BAR_LEFT;
            case BAR_LEFT -> ClientConfig.hudLocation = HudLocation.BAR_RIGHT;
            case BAR_RIGHT -> ClientConfig.hudLocation = HudLocation.CURSOR_ABOVE;
            case CURSOR_ABOVE -> ClientConfig.hudLocation = HudLocation.CURSOR_RIGHT;
            case CURSOR_RIGHT -> ClientConfig.hudLocation = HudLocation.CURSOR_BELOW;
            case CURSOR_BELOW -> ClientConfig.hudLocation = HudLocation.CURSOR_LEFT;
            case CURSOR_LEFT -> ClientConfig.hudLocation = HudLocation.BAR_CENTER;
        }
        ClientConfig.saveHudLocation();
    }
}
