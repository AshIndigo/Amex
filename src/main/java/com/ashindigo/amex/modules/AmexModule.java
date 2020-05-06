package com.ashindigo.amex.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Arrays;

/**
 * A class to represent an Armor module, tag data can be used for additional configuration
 */
public class AmexModule {
    public final Identifier name;
    private final ItemStack itemStack;
    private final EquipmentSlot[] equipmentSlots;
    private final boolean config;

    public AmexModule(Identifier name, ItemStack itemStack, EquipmentSlot[] equipmentSlots, boolean config) {
        this.name = name;
        this.itemStack = itemStack;
        this.equipmentSlots = equipmentSlots;
        this.config = config;
    }

    public void onTick(ItemStack stack, Entity entity) {
        // NO-OP by default
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
}
