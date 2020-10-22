package com.ashindigo.amex;

import com.ashindigo.amex.item.AmexArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmexItems {

    public static void registerItems() {
        registerArmorSet("iron", ArmorMaterials.IRON, Helpers.fillArray(4, 10));
        registerArmorSet("gold", ArmorMaterials.GOLD, Helpers.fillArray(4, 20));
    }

    private static void registerArmorSet(String name, ArmorMaterial material, int[] complexity) {
        Registry.register(Registry.ITEM, new Identifier(Amex.MODID, name + "_helmet"), new AmexArmor(material, EquipmentSlot.HEAD, complexity[0]));
        Registry.register(Registry.ITEM, new Identifier(Amex.MODID, name + "_chestplate"), new AmexArmor(material, EquipmentSlot.CHEST, complexity[1]));
        Registry.register(Registry.ITEM, new Identifier(Amex.MODID, name + "_leggings"), new AmexArmor(material, EquipmentSlot.LEGS, complexity[2]));
        Registry.register(Registry.ITEM, new Identifier(Amex.MODID, name + "_boots "), new AmexArmor(material, EquipmentSlot.FEET, complexity[3]));
    }
}
