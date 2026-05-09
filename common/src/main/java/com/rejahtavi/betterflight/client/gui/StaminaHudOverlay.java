package com.rejahtavi.betterflight.client.gui;

import com.rejahtavi.betterflight.BetterFlight;
import com.rejahtavi.betterflight.client.ClientConfig;
import com.rejahtavi.betterflight.client.ClientData;
import com.rejahtavi.betterflight.events.ClientEvents;
import com.rejahtavi.betterflight.util.InputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public final class StaminaHudOverlay {
    private static final int OFFSET_FROM_CURSOR = 24;
    private static final int OFFSET_TO_HOTBAR_SIDES = 105;
    private static final int OFFSET_ABOVE_HOTBAR = 44;
    private static final int SPRITE_WIDTH = 9;
    private static final int SPRITE_HEIGHT = 9;
    private static final int SPRITE_STEP = SPRITE_WIDTH - 1;
    private static final int METER_WIDTH = SPRITE_WIDTH + (9 * SPRITE_STEP);
    private static final int NONE = 0;
    private static final int FILL_FULL = 9;
    private static final int FILL_HALF = 18;
    private static final int WHITE_OUTLINE = 27;
    private static final int FLARE_OUTLINE = 36;
    private static final int RED_OUTLINE = 45;
    private static final int FULL_DURABILITY = 0;
    private static final int HALF_DURABILITY = 9;
    private static final int QUARTER_DURABILITY = 18;
    private static final int LOW_DURABILITY = 27;

    private static final Random random = new Random(System.currentTimeMillis());
    private static final ResourceLocation STAMINA_ICONS = ResourceLocation.fromNamespaceAndPath(BetterFlight.MODID, "textures/elytraspritesheet.png");
    private static int shakeEffectTimer = 0;
    private static int regenEffectTimer = 0;

    private StaminaHudOverlay() {
    }

    public static void renderOverlay(GuiGraphics guiGraphics) {
        if (!ClientData.isFlightEnabled() || !ClientData.isWearingFunctionalWings() || ClientConfig.classicHudStyle) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui || minecraft.player == null || minecraft.player.isPassenger()) {
            return;
        }

        int scaleWidth = minecraft.getWindow().getGuiScaledWidth();
        int scaleHeight = minecraft.getWindow().getGuiScaledHeight();
        int x = getWidgetX(scaleWidth);
        int y = getWidgetY(scaleHeight);
        int shakeX = 0;
        int shakeY = 0;
        int durability = getDurabilityState(ClientEvents.elytraDurability);

        if (durability == LOW_DURABILITY && minecraft.level != null && !minecraft.isPaused()) {
            long thisTick = minecraft.level.getGameTime();
            if (shakeEffectTimer > 0) {
                if (thisTick % 3 == 0) {
                    shakeX = random.nextInt(2) - 1;
                } else if (thisTick % 3 == 1) {
                    shakeY = random.nextInt(2) - 1;
                }
                shakeEffectTimer--;
            } else if (thisTick % 20 == 0) {
                shakeEffectTimer = 20;
            }
        } else {
            shakeEffectTimer = 0;
        }

        for (int i = 0; i < 10; i++) {
            guiGraphics.blit(STAMINA_ICONS, getXPos(x, i) + shakeX, y + shakeY, NONE, durability, SPRITE_WIDTH, SPRITE_HEIGHT, 256, 256);
        }

        for (int i = 0; i < 10; i++) {
            if ((i + 1) <= Math.ceil((double) InputHandler.charge / 2.0D) && InputHandler.charge > 0) {
                int type = (i + 1 == Math.ceil((double) InputHandler.charge / 2.0D) && InputHandler.charge % 2 != 0) ? FILL_HALF : FILL_FULL;
                guiGraphics.blit(STAMINA_ICONS, getXPos(x, i) + shakeX, y + shakeY, type, durability, SPRITE_WIDTH, SPRITE_HEIGHT, 256, 256);
            }
        }

        if (ClientData.isFlaring()) {
            drawOutline(guiGraphics, x, y, shakeX, shakeY, durability, FLARE_OUTLINE);
        }
        if (shakeEffectTimer > 0) {
            drawOutline(guiGraphics, x, y, shakeX, shakeY, durability, RED_OUTLINE);
        }
        if (regenEffectTimer > 0) {
            drawOutline(guiGraphics, x, y, shakeX, shakeY, durability, WHITE_OUTLINE);
            regenEffectTimer--;
        }
    }

    public static void startRegenAnimation() {
        if (regenEffectTimer == 0) {
            regenEffectTimer = 10;
        }
    }

    private static void drawOutline(GuiGraphics guiGraphics, int x, int y, int shakeX, int shakeY, int durability, int outline) {
        for (int i = 0; i < 10; i++) {
            guiGraphics.blit(STAMINA_ICONS, getXPos(x, i) + shakeX, y + shakeY, outline, durability, SPRITE_WIDTH, SPRITE_HEIGHT, 256, 256);
        }
    }

    private static int getDurabilityState(double durability) {
        if (durability > 0.95f) return LOW_DURABILITY;
        if (durability > 0.75f) return QUARTER_DURABILITY;
        if (durability > 0.50f) return HALF_DURABILITY;
        return FULL_DURABILITY;
    }

    private static int getXPos(int x, int i) {
        return x + (i * SPRITE_STEP);
    }

    private static int getWidgetX(int scaleWidth) {
        return switch (ClientConfig.hudLocation) {
            case CURSOR_LEFT -> scaleWidth / 2 - OFFSET_FROM_CURSOR - METER_WIDTH;
            case CURSOR_RIGHT -> scaleWidth / 2 + OFFSET_FROM_CURSOR;
            case BAR_LEFT -> scaleWidth / 2 - OFFSET_TO_HOTBAR_SIDES - METER_WIDTH / 2;
            case BAR_RIGHT -> scaleWidth / 2 + OFFSET_TO_HOTBAR_SIDES - METER_WIDTH / 2;
            default -> scaleWidth / 2 - METER_WIDTH / 2;
        };
    }

    private static int getWidgetY(int scaleHeight) {
        return switch (ClientConfig.hudLocation) {
            case CURSOR_ABOVE -> scaleHeight / 2 - OFFSET_FROM_CURSOR - SPRITE_HEIGHT;
            case CURSOR_BELOW -> scaleHeight / 2 + OFFSET_FROM_CURSOR;
            case CURSOR_LEFT, CURSOR_RIGHT -> scaleHeight / 2 - SPRITE_HEIGHT / 2;
            default -> scaleHeight - OFFSET_ABOVE_HOTBAR - SPRITE_HEIGHT / 2;
        };
    }
}
