package com.ashindigo.amex.modules;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class AmexArmorModule extends AmexModule {
    private final float[] protection;
    private final float[] toughness;

    public AmexArmorModule(Identifier name, ItemStack itemStack, float[] protection, float[] toughness, EquipmentSlot[] equipmentSlots, boolean config) {
        super(name, itemStack, equipmentSlots, config);
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
