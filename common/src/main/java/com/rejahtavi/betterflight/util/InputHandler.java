package com.rejahtavi.betterflight.util;

import com.rejahtavi.betterflight.client.ClientConfig;
import com.rejahtavi.betterflight.client.ClientData;
import com.rejahtavi.betterflight.client.Keybinding;
import com.rejahtavi.betterflight.client.gui.ClassicHudOverlay;
import com.rejahtavi.betterflight.client.gui.StaminaHudOverlay;
import com.rejahtavi.betterflight.common.BetterFlightCommonConfig;
import com.rejahtavi.betterflight.common.FlightActionType;
import com.rejahtavi.betterflight.network.FlightMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.lwjgl.glfw.GLFW;

import java.util.stream.Stream;

public final class InputHandler {
    private static int rechargeTickCounter = 0;
    private static int flareTickCounter = 0;
    public static int charge = BetterFlightCommonConfig.maxCharge;

    private InputHandler() {
    }

    public static boolean classicFlight(Player player) {
        if (canTakeOff(player)) {
            return classicTakeOff(player);
        }
        if (canFlap(player)) {
            return classicFlap(player);
        }
        return false;
    }

    public static boolean modernFlight(Player player) {
        if (canFlap(player) && spendCharge(player, BetterFlightCommonConfig.flapCost)) {
            if (!checkForAir(player.level(), player)) {
                FlightHandler.handleModernBoost(player);
            } else {
                FlightHandler.handleModernFlap(player);
            }
            return true;
        }
        return false;
    }

    public static void handleRecharge(Player player) {
        if (player.isCreative()) {
            charge = BetterFlightCommonConfig.maxCharge;
            return;
        }

        int chargeThreshold = player.onGround() ? BetterFlightCommonConfig.rechargeTicksOnGround : BetterFlightCommonConfig.rechargeTicksInAir;
        if (rechargeTickCounter < chargeThreshold) {
            rechargeTickCounter++;
        }

        if (!ClientData.isFlaring() && rechargeTickCounter >= chargeThreshold && charge < BetterFlightCommonConfig.maxCharge
                && player.getFoodData().getFoodLevel() > BetterFlightCommonConfig.minFood) {
            charge++;
            rechargeTickCounter = 0;
            ClassicHudOverlay.setRechargeBorderTimer(ClientConfig.BORDER_FLASH_TICKS);
            StaminaHudOverlay.startRegenAnimation();
            FlightMessages.sendToServer(FlightActionType.RECHARGE);
        }
    }

    public static void tryFlare(Player player) {
        if (ClientData.isWearingFunctionalWings()
                && ClientData.isFlightEnabled()
                && isFlareKeyDown()
                && ((player.isCreative() || charge > 0) || player.isInWater() || player.isInLava())
                && !player.onGround()
                && player.isFallFlying()) {
            if (player.isInWater() || player.isInLava()) {
                FlightHandler.handleFlightStop();
                return;
            }

            FlightHandler.handleFlare(player);
            flareTickCounter++;
            ClientData.setIsFlaring(true);
            if (flareTickCounter >= BetterFlightCommonConfig.flareTicksPerChargePoint) {
                spendCharge(player, 1);
                flareTickCounter = 0;
            }
        } else {
            if (flareTickCounter > 0) {
                flareTickCounter--;
            }
            ClientData.setIsFlaring(false);
        }
    }

    public static ElytraData findWings(Player player) {
        ItemStack itemStack = findWingsItemStack(player);
        if (itemStack == null) {
            return null;
        }
        int durabilityRemaining = itemStack.getMaxDamage() - itemStack.getDamageValue();
        float durabilityPercent = (float) itemStack.getDamageValue() / (float) itemStack.getMaxDamage();
        return new ElytraData(itemStack, durabilityRemaining, durabilityPercent);
    }

    public static boolean checkForAir(Level world, LivingEntity player) {
        AABB boundingBox = player.getBoundingBox()
                .setMaxY(player.getBoundingBox().minY + 3.5)
                .inflate(1D, 0D, 1D)
                .move(0, -1.5D, 0);
        Stream<BlockPos> blocks = getBlockPosIfLoaded(world, boundingBox);
        Stream<BlockPos> filteredBlocks = blocks.filter(pos -> {
            BlockState block = world.getBlockState(pos);
            return block.isCollisionShapeFullBlock(world, pos) || !block.getFluidState().isEmpty();
        });
        return filteredBlocks.toList().isEmpty();
    }

    private static boolean canFlap(Player player) {
        return ClientData.isWearingFunctionalWings() && !player.onGround() && player.isFallFlying();
    }

    private static boolean isFlareKeyDown() {
        Minecraft minecraft = Minecraft.getInstance();
        return Keybinding.flareKey.isDown()
                || minecraft.getWindow() != null && com.mojang.blaze3d.platform.InputConstants.isKeyDown(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_X);
    }

    private static boolean canTakeOff(Player player) {
        return ClientData.isWearingFunctionalWings()
                && ClientData.getOffGroundTicks() > BetterFlightCommonConfig.TAKE_OFF_JUMP_DELAY
                && player.isSprinting()
                && !player.isFallFlying()
                && player.getDeltaMovement().length() > BetterFlightCommonConfig.TAKE_OFF_SPEED;
    }

    private static boolean classicTakeOff(Player player) {
        if (spendCharge(player, BetterFlightCommonConfig.takeOffCost)) {
            FlightHandler.handleClassicTakeoff(player);
            return true;
        }
        return false;
    }

    private static boolean classicFlap(Player player) {
        if (spendCharge(player, BetterFlightCommonConfig.flapCost)) {
            FlightHandler.handleClassicFlap(player);
            return true;
        }
        return false;
    }

    private static boolean spendCharge(Player player, int points) {
        if (player.isCreative()) {
            return true;
        }
        if (charge < points) {
            return false;
        }
        charge = Math.max(0, charge - points);
        rechargeTickCounter = 0;
        ClientData.setCooldown(BetterFlightCommonConfig.cooldownTicks);
        ClassicHudOverlay.setDepletionBorderTimer(ClientConfig.BORDER_FLASH_TICKS);
        return true;
    }

    private static ItemStack findWingsItemStack(Player player) {
        ItemStack stack = WingProviders.findWings(player);
        return stack.isEmpty() ? null : stack;
    }

    private static Stream<BlockPos> getBlockPosIfLoaded(Level world, AABB boundingBox) {
        int i = Mth.floor(boundingBox.minX);
        int j = Mth.floor(boundingBox.maxX);
        int k = Mth.floor(boundingBox.minY);
        int l = Mth.floor(boundingBox.maxY);
        int i1 = Mth.floor(boundingBox.minZ);
        int j1 = Mth.floor(boundingBox.maxZ);
        if (l < world.getMinBuildHeight() || k >= world.getMaxBuildHeight()) {
            return Stream.empty();
        }
        for (int x = SectionPos.blockToSectionCoord(i); x <= SectionPos.blockToSectionCoord(j); x++) {
            for (int z = SectionPos.blockToSectionCoord(i1); z <= SectionPos.blockToSectionCoord(j1); z++) {
                if (!world.hasChunk(x, z)) {
                    return Stream.empty();
                }
            }
        }
        return BlockPos.betweenClosedStream(boundingBox);
    }
}
