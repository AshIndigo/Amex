package com.ashindigo.amex;

import com.ashindigo.amex.item.AmexArmor;
import com.ashindigo.amex.modules.AmexArmorModule;
import com.ashindigo.amex.modules.AmexGeneratorModule;
import com.ashindigo.amex.modules.AmexModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A registry for every base module, also includes various important methods used elsewhere in the mod
 */
public class ModuleManager {

    public static final String LISTNAME = "modules";
    public static final int TYPE = 10;
    public static final String MODKEY = "name";
    public static final String CONFIGKEY = "power";
    public static final String ENERGYKEY = "energy";

    private static final EquipmentSlot[] allSlots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    private static final HashMap<Identifier, AmexModule> values = new HashMap<>();
    private static final HashMap<Identifier, AmexModule> armorVals = new HashMap<>();
    // Modules
    public static final AmexArmorModule IRON_PLATING = (AmexArmorModule) register(new AmexArmorModule(new Identifier(AmexMod.MODID, "iron_plating"), new ItemStack(ItemRegistry.IRON_PLATING), new float[]{2, 5, 6, 2}, HelperMethods.fillArray(ArmorMaterials.IRON.getToughness(), 4), allSlots, false, () -> AmexMod.config.powerUsageValues.ironPlatingPower));
    public static final AmexArmorModule DIAMOND_PLATING = (AmexArmorModule) register(new AmexArmorModule(new Identifier(AmexMod.MODID, "diamond_plating"), new ItemStack(ItemRegistry.DIAMOND_PLATING), new float[]{3, 6, 8, 3}, HelperMethods.fillArray(ArmorMaterials.DIAMOND.getToughness(), 4), allSlots, false, () -> AmexMod.config.powerUsageValues.diamondPlatingPower));
    public static final AmexModule REBREATHER = register(new AmexModule(new Identifier(AmexMod.MODID, "rebreather"), new ItemStack(ItemRegistry.REBREATHER), new EquipmentSlot[]{EquipmentSlot.HEAD}, false, () -> AmexMod.config.powerUsageValues.rebreatherPower) {
        @Override
        public void onTick(ItemStack stack, PlayerEntity entity) {
            if (entity.getAir() < 50) {
                entity.setAir(entity.getMaxAir());
                ModuleManager.takePowerWithCheck(entity, powerUsage());
            }
        }
    });
    public static final AmexModule ELYTRA = register(new AmexModule(new Identifier(AmexMod.MODID, "elytra"), new ItemStack(Items.ELYTRA), new EquipmentSlot[]{EquipmentSlot.CHEST}, false, () -> AmexMod.config.powerUsageValues.elytraUsage) {
        @Override
        public void onTick(ItemStack stack, PlayerEntity entity) {
            if (entity != null) {
                if (entity.checkFallFlying()) {
                    super.onTick(stack, entity);
                }
            }
        }
    });
    public static final AmexModule SPEED = register(new AmexModule(new Identifier(AmexMod.MODID, "speed"), new ItemStack(ItemRegistry.ARTIFICIAL_MUSCLE), new EquipmentSlot[]{EquipmentSlot.LEGS}, true, () -> AmexMod.config.powerUsageValues.speedUsage) {
        @Override
        public void onTick(ItemStack stack, PlayerEntity entity) {
            if (entity.getVelocity().x < 1 || entity.getVelocity().z < 0.04) {
                super.onTick(stack, entity);
                ModuleManager.takePowerWithCheck(entity, powerUsage());
            }
        }
    });
    public static final AmexModule DAMAGE = register(new AmexModule(new Identifier(AmexMod.MODID, "damage"), new ItemStack(ItemRegistry.ARTIFICIAL_MUSCLE), new EquipmentSlot[]{EquipmentSlot.CHEST}, true, () -> AmexMod.config.powerUsageValues.damageUsage));
    public static final AmexModule JUMP = register(new AmexModule(new Identifier(AmexMod.MODID, "jump"), new ItemStack(ItemRegistry.ARTIFICIAL_MUSCLE), new EquipmentSlot[]{EquipmentSlot.FEET}, true, () -> AmexMod.config.powerUsageValues.jumpUsage) {
        @Override
        public void onTick(ItemStack stack, PlayerEntity entity) {
            // NO-OP
        }
    });
    public static final AmexModule FALL_RESIST = register(new AmexModule(new Identifier(AmexMod.MODID, "fall_resist"), new ItemStack(ItemRegistry.FALL_PADS), new EquipmentSlot[]{EquipmentSlot.FEET}, false, () -> AmexMod.config.powerUsageValues.fallResistUsage) {
        @Override
        public void onTick(ItemStack stack, PlayerEntity entity) {
            // NO-OP
        }
    });

    // HUD
    public static final AmexModule COMPASS = register(new AmexModule(new Identifier(AmexMod.MODID, "compass"), new ItemStack(Items.COMPASS), new EquipmentSlot[]{EquipmentSlot.HEAD}, false, () -> AmexMod.config.powerUsageValues.compassUsage));
    public static final AmexModule CLOCK = register(new AmexModule(new Identifier(AmexMod.MODID, "clock"), new ItemStack(Items.CLOCK), new EquipmentSlot[]{EquipmentSlot.HEAD}, false, () -> AmexMod.config.powerUsageValues.clockUsage));
    public static final AmexModule LIGHT = register(new AmexModule(new Identifier(AmexMod.MODID, "light"), new ItemStack(Items.DAYLIGHT_DETECTOR), new EquipmentSlot[]{EquipmentSlot.HEAD}, false, () -> AmexMod.config.powerUsageValues.lightUsage));

    // Generators
    public static final AmexGeneratorModule HEAT = (AmexGeneratorModule) register(new AmexGeneratorModule(new Identifier(AmexMod.MODID, "heat_gen"), new ItemStack(ItemRegistry.HEAT_GEN), new EquipmentSlot[]{EquipmentSlot.CHEST}, false, () -> AmexMod.config.generatorValues.heatGenerator) {
        @Override
        public void onTick(ItemStack stack, PlayerEntity entity) {
            // Gotta check if entity is close to heat
        }
    });
    public static final AmexGeneratorModule SOLAR = (AmexGeneratorModule) register(new AmexGeneratorModule(new Identifier(AmexMod.MODID, "solar_gen"), new ItemStack(ItemRegistry.SOLAR_GEN), new EquipmentSlot[]{EquipmentSlot.HEAD}, false, () -> AmexMod.config.generatorValues.solarGenerator) {
        @Override
        public void onTick(ItemStack stack, PlayerEntity entity) {
            if (entity.world.isDay() && entity.world.isSkyVisible(entity.getBlockPos())) {
                ModuleManager.addPowerWithCheck(entity, getPowerGen());
            }
        }
    });
    public static final AmexGeneratorModule KINETIC = (AmexGeneratorModule) register(new AmexGeneratorModule(new Identifier(AmexMod.MODID, "kinetic_gen"), new ItemStack(ItemRegistry.KINETIC_GEN), new EquipmentSlot[]{EquipmentSlot.LEGS}, false, () -> AmexMod.config.generatorValues.kineticGenerator) {
        @Override
        public void onTick(ItemStack stack, PlayerEntity entity) {
            if (entity.getVelocity().x > 0.04 || entity.getVelocity().z > 0.04) {
                super.onTick(stack, entity);
            }
        }
    });

    /**
     * Registers an AmexModule to the internal lists
     *
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
     *
     * @param id Identifier for given module
     * @return If the given module is an armor module
     */
    public static boolean isArmor(Identifier id) {
        return armorVals.containsKey(id);
    }

    /**
     * Get a module for the given Identifier
     *
     * @param name The Identifier for the module to be retrieved
     * @return The retrieved module
     */
    public static AmexModule getModule(Identifier name) {
        return values.get(name);
    }

    /**
     * Gets a list of modules that can be installed in the given EquipmentSlot
     *
     * @param slot The equipment slot to get all modules for
     * @return A list of valid modules for that slot
     */
    public static List<AmexModule> getAllModulesForSlot(EquipmentSlot slot) {
        return values.values().stream().filter(amexModule -> amexModule.isValidForSlot(slot)).collect(Collectors.toList());

    }

    /**
     * Add the given module to the given armor piece
     *
     * @param armor  The armor to install the module onto
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
        if (armor.hasTag()) {
            armor.getTag().put(LISTNAME, tag);
        }
    }

    /**
     * Removes the given module to the given armor piece
     *
     * @param armor  The armor that will have the given module removed from
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
        if (armor.hasTag()) {
            armor.getTag().put(LISTNAME, tag);
        }
    }

    /**
     * Does the given armor have the given module installed
     *
     * @param armor  Armor to check
     * @param module Module to check if installed
     * @return If the module is installed in the given armor piece
     */
    public static boolean hasModule(ItemStack armor, AmexModule module) {
        ListTag tag = armor.getOrCreateTag().getList(LISTNAME, TYPE);
        return hasModule(tag, module);
    }

    /**
     * Does the given armor have the given module installed
     *
     * @param tag    Tag to check
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
    }

    /**
     * Get a list of currently installed modules
     *
     * @param armor Armor piece to check
     * @return A list of currently installed modules
     */
    public static List<AmexModule> getModulesOnArmor(ItemStack armor) {
        ListTag tag = armor.getOrCreateTag().getList(LISTNAME, TYPE);
        ArrayList<AmexModule> collect = tag.stream().filter(cTag -> cTag instanceof CompoundTag).map(cTag -> getModule(Identifier.tryParse(((CompoundTag) cTag).getString(MODKEY)))).collect(Collectors.toCollection(ArrayList::new));
        collect.removeIf((Objects::isNull));
        return collect;
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

    public static boolean takePowerWithCheck(PlayerEntity entity, int powerUsage) {
        if (getTotalPower(entity) > powerUsage) {
            takePower(entity, powerUsage);
            entity.inventory.markDirty();
            return true;
        }
        return false;
    }

    static void takePower(PlayerEntity entity, int powerUsage) {
        int remPower = powerUsage;
        if (entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof AmexArmor) {
            remPower = takePowerStack(entity.getEquippedStack(EquipmentSlot.HEAD), remPower);
        }
        if (remPower == 0) return;
        if (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof AmexArmor) {
            remPower = takePowerStack(entity.getEquippedStack(EquipmentSlot.CHEST), remPower);
        }
        if (remPower == 0) return;
        if (entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof AmexArmor) {
            remPower = takePowerStack(entity.getEquippedStack(EquipmentSlot.LEGS), remPower);
        }
        if (remPower == 0) return;
        if (entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof AmexArmor) {
            takePowerStack(entity.getEquippedStack(EquipmentSlot.FEET), remPower);
        }
    }

    static int takePowerStack(ItemStack stack, int power) {
        if (stack.getOrCreateTag().getInt(ENERGYKEY) > 0) {
            power = stack.getOrCreateTag().getInt(ENERGYKEY) - power;
            stack.getOrCreateTag().putInt(ENERGYKEY, Math.max(0, stack.getOrCreateTag().getInt(ENERGYKEY) - power));

            if (power >= 0) {
                return 0;
            } else {
                power *= -1;
            }
        }
        return power;
    }

    public static boolean addPowerWithCheck(PlayerEntity entity, int powerUsage) {
        if (!entity.world.isClient && getTotalPower(entity) == getMaxPower(entity)) {
            return false;
        }
        addPower(entity, powerUsage);
        entity.inventory.markDirty();
        return false;
    }


    static void addPower(PlayerEntity entity, int powerUsage) {
        int remPower = powerUsage;
        if (entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof AmexArmor && getCurrentPower(entity.getEquippedStack(EquipmentSlot.HEAD)) < AmexMod.config.maxPower) {
            remPower = addPowerStack(entity.getEquippedStack(EquipmentSlot.HEAD), remPower);
        }
        if (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof AmexArmor && getCurrentPower(entity.getEquippedStack(EquipmentSlot.CHEST)) < AmexMod.config.maxPower) {
            remPower = addPowerStack(entity.getEquippedStack(EquipmentSlot.CHEST), remPower);
        }
        if (entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof AmexArmor && getCurrentPower(entity.getEquippedStack(EquipmentSlot.LEGS)) < AmexMod.config.maxPower) {
            remPower = addPowerStack(entity.getEquippedStack(EquipmentSlot.LEGS), remPower);
        }
        if (entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof AmexArmor && getCurrentPower(entity.getEquippedStack(EquipmentSlot.FEET)) < AmexMod.config.maxPower) {
            addPowerStack(entity.getEquippedStack(EquipmentSlot.FEET), remPower);
        }
    }

    public static int getCurrentPower(ItemStack equippedStack) {
        return equippedStack.getOrCreateTag().getInt(ENERGYKEY);
    }

    static int addPowerStack(ItemStack stack, int power) {
        //if (stack.getOrCreateTag().getInt(ENERGYKEY) > 0) {
            power = (stack.getOrCreateTag().getInt(ENERGYKEY) + power) - AmexMod.config.maxPower;
            stack.getOrCreateTag().putInt(ENERGYKEY, Math.max(AmexMod.config.maxPower, stack.getOrCreateTag().getInt(ENERGYKEY) + power));
            return Math.max(power, 0);
        //}
        //return power;
    }

    public static boolean hasPower(PlayerEntity entity, int power) {
        return getTotalPower(entity) >= power;
    }

    public static int getTotalPower(PlayerEntity entity) {
        int power = 0;
        for (ItemStack stack : entity.getArmorItems()) {
            power += stack.getItem() instanceof AmexArmor ? (stack.getOrCreateTag().getInt(ENERGYKEY)) : 0;
        }
        return power;
    }

    public static int getMaxPower(PlayerEntity entity) {
        int max = 0;
        if (entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof AmexArmor) {
            max += AmexMod.config.maxPower;
        }
        if (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof AmexArmor) {
            max += AmexMod.config.maxPower;
        }
        if (entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof AmexArmor) {
            max += AmexMod.config.maxPower;
        }
        if (entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof AmexArmor) {
            max += AmexMod.config.maxPower;
        }
        return max;
    }

    public static int getArmorPowerNeed(PlayerEntity player, EquipmentSlot slot) {
        return getModulesOnArmor(player.getEquippedStack(slot)).stream().filter(module -> module instanceof AmexArmorModule).mapToInt(AmexModule::powerUsage).sum();
    }
}