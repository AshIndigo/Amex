package com.ashindigo.amex.item;

import com.ashindigo.amex.Amex;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;

public class AmexArmor extends ArmorItem {

    private final int complexity;

    public AmexArmor(ArmorMaterial material, EquipmentSlot slot, int complexity) {
        super(material, slot, new Item.Settings().maxCount(1).group(Amex.AMEX_GROUP));
        this.complexity = complexity;
    }

    public int getComplexity() {
        return complexity;
    }
}
