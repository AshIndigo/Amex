package com.ashindigo.amex.power;

import com.ashindigo.amex.AmexMod;
import com.ashindigo.amex.item.AmexArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class PowerManager {

    public static final String ENERGYKEY = "energy";

    public static void givePlayerPower(PlayerEntity player, int power) {
        int powerLeft = power;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getEquippedStack(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof AmexArmor) {
                    if (powerLeft > 0) {
                        powerLeft = powerLeft - addEnergy(stack, powerLeft, false);
                    } else
                        break;
                }
            }
        }
    }

    private static int addEnergy(ItemStack stack, int power, boolean sim) { // Look sim is just there for potential usefulness, might just remove it later if I deem it pointless
        int energyLeft = Math.min(getMaxPower() - getPower(stack), power);
        if (!sim) {
            if (energyLeft != 0) {
                stack.getOrCreateTag().putInt(ENERGYKEY, getPower(stack) + energyLeft);
            }
        }
        return energyLeft;
    }

    public static void takePlayerPower(PlayerEntity player, int power) {
        if (player.world.isClient) {
            return;
        }
        int remaining = power;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getEquippedStack(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof AmexArmor) {
                    if (remaining > 0) {
                        remaining = remaining - removeEnergy(stack, remaining, false);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private static int removeEnergy(ItemStack stack, int power, boolean sim) {
        int energyLeft = Math.min(getPower(stack), power);
        if (!sim) {
            if (energyLeft != 0) {
                stack.getOrCreateTag().putInt(ENERGYKEY, getPower(stack) - energyLeft); // Subtract whatever can be done from the total
            }
        }
        return energyLeft;
    }

    public static int getPower(ItemStack stack) {
        return stack.getOrCreateTag().getInt(ENERGYKEY);
    }

    public static int getMaxPower() {
        return AmexMod.config.maxPower;
    }

    public static int getMaxPowerPlayer(PlayerEntity player) {
        int max = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getEquippedStack(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof AmexArmor) {
                    max += AmexMod.config.maxPower;
                }
            }
        }
        return max;
    }

    public static int getPlayerPower(PlayerEntity player) {
        int total = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getEquippedStack(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof AmexArmor) {
                    total += getPower(stack);
                }
            }
        }
        return total;
    }
}
