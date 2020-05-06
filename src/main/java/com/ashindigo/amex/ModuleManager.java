package com.ashindigo.amex;

import com.ashindigo.amex.modules.AmexArmorModule;
import com.ashindigo.amex.modules.AmexModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A registry for every base module, also includes various important methods used elsewhere in the mod
 */
public class ModuleManager {

    public static final String LISTNAME = "modules";
    public static final int TYPE = 10;
    public static final String MODKEY = "name";
    public static final String CONFIGKEY = "power";

    private static final EquipmentSlot[] allSlots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    private static final HashMap<Identifier, AmexModule> values = new HashMap<>();
    private static final HashMap<Identifier, AmexModule> armorVals = new HashMap<>();
    // Modules
    public static final AmexArmorModule IRON_PLATING = (AmexArmorModule) register(new AmexArmorModule(new Identifier(AmexMod.MODID, "iron_plating"), new ItemStack(ItemRegistry.IRON_PLATING), new float[]{2, 5, 6, 2}, HelperMethods.fillArray(ArmorMaterials.IRON.getToughness(), 4), allSlots, false));
    public static final AmexArmorModule DIAMOND_PLATING = (AmexArmorModule) register(new AmexArmorModule(new Identifier(AmexMod.MODID, "diamond_plating"), new ItemStack(ItemRegistry.DIAMOND_PLATING), new float[]{3, 6, 8, 3}, HelperMethods.fillArray(ArmorMaterials.DIAMOND.getToughness(), 4), allSlots, false));
    public static final AmexModule REBREATHER = register(new AmexModule(new Identifier(AmexMod.MODID, "rebreather"), new ItemStack(ItemRegistry.REBREATHER), new EquipmentSlot[]{EquipmentSlot.HEAD}, false) {
        @Override
        public void onTick(ItemStack stack, Entity entity) { // TODO Hmm, implementation, how should I do this?
            if (entity.getAir() < 50) {
                entity.setAir(entity.getMaxAir());
            }
            super.onTick(stack, entity);
        }
    });
    public static final AmexModule ELYTRA = register(new AmexModule(new Identifier(AmexMod.MODID, "elytra"), new ItemStack(Items.ELYTRA), new EquipmentSlot[]{EquipmentSlot.CHEST}, false));
    public static final AmexModule SPEED = register(new AmexModule(new Identifier(AmexMod.MODID, "speed"), new ItemStack(ItemRegistry.ARTIFICIAL_MUSCLE), new EquipmentSlot[]{EquipmentSlot.LEGS}, true));
    public static final AmexModule DAMAGE = register(new AmexModule(new Identifier(AmexMod.MODID, "damage"), new ItemStack(ItemRegistry.ARTIFICIAL_MUSCLE), new EquipmentSlot[]{EquipmentSlot.CHEST}, true));
    public static final AmexModule JUMP = register(new AmexModule(new Identifier(AmexMod.MODID, "jump"), new ItemStack(ItemRegistry.ARTIFICIAL_MUSCLE), new EquipmentSlot[]{EquipmentSlot.FEET}, true));
    public static final AmexModule FALL_RESIST = register(new AmexModule(new Identifier(AmexMod.MODID, "fall_resist"), new ItemStack(ItemRegistry.FALL_PADS), new EquipmentSlot[]{EquipmentSlot.FEET}, false));

    // HUD
    public static final AmexModule COMPASS = register(new AmexModule(new Identifier(AmexMod.MODID, "compass"), new ItemStack(Items.COMPASS), new EquipmentSlot[]{EquipmentSlot.HEAD}, false));
    public static final AmexModule CLOCK = register(new AmexModule(new Identifier(AmexMod.MODID, "clock"), new ItemStack(Items.CLOCK), new EquipmentSlot[]{EquipmentSlot.HEAD}, false));
    public static final AmexModule LIGHT = register(new AmexModule(new Identifier(AmexMod.MODID, "light"), new ItemStack(Items.DAYLIGHT_DETECTOR), new EquipmentSlot[]{EquipmentSlot.HEAD}, false));

    /**
     * Registers an AmexModule to the internal lists
     * @param module The module to register
     * @return The given module, so calls can be chained
     */
    public static AmexModule register(AmexModule module) {
        values.put(module.getName(), module);
        if (module instanceof AmexArmorModule) {
            armorVals.put(module.getName(), module);
        }
        return module;
    }

    /**
     * If the module is an armor module
     * @param id Identifier for given module
     * @return If the given module is an armor module
     */
    public static boolean isArmor(Identifier id) {
        return armorVals.containsKey(id);
    }

    /**
     * Get a module for the given Identifier
     * @param name The Identifier for the module to be retrieved
     * @return The retrieved module
     */
    public static AmexModule getModule(Identifier name) {
        return values.get(name);
    }

    /**
     * Gets a list of modules that can be installed in the given EquipmentSlot
     * @param slot The equipment slot to get all modules for
     * @return A list of valid modules for that slot
     */
    public static List<AmexModule> getAllModulesForSlot(EquipmentSlot slot) {
        return values.values().stream().filter(amexModule -> amexModule.isValidForSlot(slot)).collect(Collectors.toList());

    }

    /**
     * Add the given module to the given armor piece
     * @param armor The Chestplate to install the module onto
     * @param module The module to install
     */
    @SuppressWarnings("ConstantConditions")
    public static void addModule(ItemStack armor, AmexModule module) {
        ListTag tag = armor.getOrCreateTag().getList(LISTNAME, TYPE);
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag cTag = tag.getCompound(i);
            if (cTag.getString(MODKEY).equals(module.getName().toString())) {
                return;
            }
        }
        CompoundTag modTag = new CompoundTag();
        modTag.putString(MODKEY, module.getName().toString());
        modTag.putInt(CONFIGKEY, 0);
        tag.add(modTag);
//        if (!tag.contains(StringTag.of(module.getName().toString()))) {
//            tag.add(StringTag.of(module.getName().toString()));
//        }
        if (armor.hasTag()) {
            armor.getTag().put(LISTNAME, tag);
        }
    }

    /**
     * Removes the given module to the given armor piece
     * @param armor The armor that will have the given module removed from
     * @param module The module to remove
     */
    @SuppressWarnings("ConstantConditions")
    public static void removeModule(ItemStack armor, AmexModule module) {
        ListTag tag = armor.getOrCreateTag().getList(LISTNAME, TYPE);
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag cTag = tag.getCompound(i);
            if (cTag.getString(MODKEY).equals(module.getName().toString())) {
                tag.remove(cTag);
            }
        }
//        if (hasModule(armor, module)) {
//            tag.remove(StringTag.of(module.getName().toString()));
//        }
        if (armor.hasTag()) {
            armor.getTag().put(LISTNAME, tag);
        }
    }

//    /**
//     * If the given tag has an armor module installed
//     * @param tag The Armor's NBT Tag
//     * @return If the given armor tag has an armor module installed
//     */
//    public static boolean hasArmorModule(CompoundTag tag) {
//        if (tag == null) return false;
//        if (tag.isEmpty()) return false;
//        if (tag.contains(LISTNAME)) {
//            ListTag modules = tag.getList(LISTNAME, ModuleManager.TYPE);
//            for (Tag stringTag : modules) {
//                StringTag strTag = (StringTag) stringTag;
//                if (ModuleManager.isArmor(Identifier.tryParse(strTag.asString()))) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * Does the given armor have the given module installed
     * @param armor Armor to check
     * @param module Module to check if installed
     * @return If the module is installed in the given armor piece
     */
    public static boolean hasModule(ItemStack armor, AmexModule module) {
        ListTag tag = armor.getOrCreateTag().getList(LISTNAME, TYPE);
        return hasModule(tag, module);
    }

    /**
     * Does the given armor have the given module installed
     * @param tag Tag to check
     * @param module Module to check if installed
     * @return If the module is installed in the given armor piece
     */
    public static boolean hasModule(ListTag tag, AmexModule module) {
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag cTag = tag.getCompound(i);
            if (cTag.getString(MODKEY).equals(module.getName().toString())) {
               return true;
            }
        }
        return false;
        //return tag.contains(StringTag.of(module.getName().toString()));
    }

    /**
     * Get a list of currently installed modules
     * @param armor Armor piece to check
     * @return A list of currently installed modules
     */
    public static List<AmexModule> getModulesOnArmor(ItemStack armor) {
        ListTag tag = armor.getOrCreateTag().getList(LISTNAME, TYPE);
        return tag.stream().filter(cTag -> cTag instanceof CompoundTag).map(cTag -> getModule(Identifier.tryParse(((CompoundTag) cTag).getString(MODKEY)))).collect(Collectors.toCollection(ArrayList::new));
    }

    public static int getConfiguredValue(ItemStack stack, AmexModule module) {
        ListTag tag = stack.getOrCreateTag().getList(LISTNAME, TYPE);
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag cTag = tag.getCompound(i);
            if (cTag.getString(MODKEY).equals(module.getName().toString())) {
               return cTag.getInt(CONFIGKEY);
            }
        }
        return 0;
    }

    public static int getConfiguredValue(CompoundTag iTag, AmexModule module) {
        ListTag tag = iTag.getList(LISTNAME, TYPE);
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag cTag = tag.getCompound(i);
            if (cTag.getString(MODKEY).equals(module.getName().toString())) {
                return cTag.getInt(CONFIGKEY);
            }
        }
        return 0;
    }

    public static void setConfiguredValue(ItemStack stack, AmexModule module, int value) {
        ListTag tag = stack.getOrCreateTag().getList(LISTNAME, TYPE);
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag cTag = tag.getCompound(i);
            if (cTag.getString(MODKEY).equals(module.getName().toString())) {
                cTag.putInt(CONFIGKEY, value);
                tag.remove(i);
                tag.add(cTag);
                return;
            }
        }
        if (stack.hasTag()) {
            stack.getOrCreateTag().put(LISTNAME, tag);
        }
    }
}