package com.ashindigo.amex.modules;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.IntSupplier;

public class AmexArmorModule extends AmexModule {
    private final float[] protection;
    private final float[] toughness;

    public AmexArmorModule(Identifier name, ItemStack itemStack, float[] protection, float[] toughness, EquipmentSlot[] equipmentSlots, boolean config, IntSupplier power) {
        super(name, itemStack, equipmentSlots, config, power);
        this.protection = protection;
        this.toughness = toughness;
    }

    public float[] getProtection() {
        return protection;
    }

    public float[] getToughness() {
        return toughness;
    }
}
