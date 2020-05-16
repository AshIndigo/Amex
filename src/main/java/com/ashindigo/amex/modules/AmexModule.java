package com.ashindigo.amex.modules;

import com.ashindigo.amex.ModuleManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.IntSupplier;

/**
 * A class to represent an Armor module, tag data can be used for additional configuration
 */
public class AmexModule {
    public final Identifier name;
    private final ItemStack itemStack;
    private final EquipmentSlot[] equipmentSlots;
    private final boolean config;
    private final IntSupplier power;

    /**
     * Defines a normal module
     * @param name The modules internal name
     * @param itemStack The component used to install the module, can be any item stack
     * @param equipmentSlots Valid armor slots for the module
     * @param config Can this module be configured
     * @param power Power usage supplier
     */
    public AmexModule(Identifier name, ItemStack itemStack, EquipmentSlot[] equipmentSlots, boolean config, IntSupplier power) {
        this.name = name;
        this.itemStack = itemStack;
        this.equipmentSlots = equipmentSlots;
        this.config = config;
        this.power = power;
    }

    /**
     * Runs every tick
     * Will by default, take power per tick.
     * @param stack Armor's itemstack
     * @param entity The entity wearing the armor
     */
    public void onTick(ItemStack stack, LivingEntity entity) {
        if (powerUsage() > 0) { // Don't bother subtracting power if it's 0
            ModuleManager.takePowerWithCheck(entity, powerUsage());
        }
    }

    public boolean isValidForSlot(EquipmentSlot slot) {
        return Arrays.stream(equipmentSlots).anyMatch(equipmentSlot -> slot == equipmentSlot);
    }

    public Identifier getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return itemStack.copy();
    }

    public boolean isConfigurable() {
        return config;
    }

    public int powerUsage() { return power.getAsInt(); }
}
