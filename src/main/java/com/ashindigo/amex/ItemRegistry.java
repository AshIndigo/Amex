package com.ashindigo.amex;

import com.ashindigo.amex.item.AmexArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    // Main Armor
    public static Item AMEX_HELM;
    public static Item AMEX_CHEST;
    public static Item AMEX_LEGS;
    public static Item AMEX_BOOTS;
    // Armor Components
    public static Item IRON_PLATING;
    public static Item DIAMOND_PLATING;
    public static Item REBREATHER;
    public static Item FALL_PADS;
    public static Item ARTIFICIAL_MUSCLE; // Used for legs and chest and boot upgrades.
    // Generators
    public static Item HEAT_GEN;
    public static Item SOLAR_GEN;
    public static Item KINETIC_GEN;

    public static void addItems() {
        AMEX_HELM = register("helm", new AmexArmor(EquipmentSlot.HEAD, new Item.Settings().maxCount(1).group(AmexMod.AMEX_GROUP).maxDamage(-1)));
        AMEX_CHEST = register("chest", new AmexArmor(EquipmentSlot.CHEST, new Item.Settings().maxCount(1).group(AmexMod.AMEX_GROUP).maxDamage(-1)));
        AMEX_LEGS = register("legs", new AmexArmor(EquipmentSlot.LEGS, new Item.Settings().maxCount(1).group(AmexMod.AMEX_GROUP).maxDamage(-1)));
        AMEX_BOOTS = register("feet", new AmexArmor(EquipmentSlot.FEET, new Item.Settings().maxCount(1).group(AmexMod.AMEX_GROUP).maxDamage(-1)));
        IRON_PLATING = register("iron_plating", new Item(new Item.Settings().group(AmexMod.AMEX_GROUP)));
        DIAMOND_PLATING = register("diamond_plating", new Item(new Item.Settings().group(AmexMod.AMEX_GROUP)));
        REBREATHER = register("rebreather", new Item(new Item.Settings().group(AmexMod.AMEX_GROUP)));
        FALL_PADS = register("fall_pads", new Item(new Item.Settings().group(AmexMod.AMEX_GROUP)));
        ARTIFICIAL_MUSCLE = register("artificial_muscle", new Item(new Item.Settings().group(AmexMod.AMEX_GROUP)));
        HEAT_GEN = register("heat_gen", new Item(new Item.Settings().group(AmexMod.AMEX_GROUP)));
        SOLAR_GEN = register("solar_gen", new Item(new Item.Settings().group(AmexMod.AMEX_GROUP)));
        KINETIC_GEN = register("kinetic_gen", new Item(new Item.Settings().group(AmexMod.AMEX_GROUP)));

    }

    private static Item register(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(AmexMod.MODID, name), item);
        return item;
    }
}
