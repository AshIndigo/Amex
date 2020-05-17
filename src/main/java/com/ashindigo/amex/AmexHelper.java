package com.ashindigo.amex;

import com.ashindigo.amex.item.AmexArmor;
import com.ashindigo.amex.modules.AmexArmorModule;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;

public class AmexHelper {

    public static double getProtection(EquipmentSlot slot, ItemStack stack) {
        if (stack.hasTag() && stack.getItem() instanceof AmexArmor) {
            return getProtection(slot, stack.getTag());
        }
        return 0.0D;
    }

    public static double getProtection(EquipmentSlot slot, CompoundTag tag) {
        if (tag == null) return 0.0;
        if (tag.isEmpty()) return 0.0;
        if (tag.contains(ModuleManager.LISTNAME)) {
            ListTag modules = tag.getList(ModuleManager.LISTNAME, ModuleManager.TYPE);
            double protection = 0.0D;
            for (Tag cTag : modules) {
                CompoundTag modTag = (CompoundTag) cTag;
                if (ModuleManager.isArmor(Identifier.tryParse(modTag.getString(ModuleManager.MODKEY)))) {
                    AmexArmorModule module = ((AmexArmorModule) ModuleManager.getModule(Identifier.tryParse(modTag.getString(ModuleManager.MODKEY))));
                    if (module.isValidForSlot(slot)) {
                        protection += module.getProtection()[slot.getEntitySlotId()];
                    }
                }
            }
            return protection;
        }
        return 0.0D;
    }

    public static double getToughness(EquipmentSlot slot, CompoundTag tag) {
        if (tag == null) return 0.0;
        if (tag.isEmpty()) return 0.0;
        if (tag.contains(ModuleManager.LISTNAME)) {
            ListTag modules = tag.getList(ModuleManager.LISTNAME, ModuleManager.TYPE);
            double toughness = 0.0D;
            for (Tag cTag : modules) {
                CompoundTag modTag = (CompoundTag) cTag;
                if (ModuleManager.isArmor(Identifier.tryParse(modTag.getString(ModuleManager.MODKEY)))) {
                    AmexArmorModule module = ((AmexArmorModule) ModuleManager.getModule(Identifier.tryParse(modTag.getString(ModuleManager.MODKEY))));
                    if (module.isValidForSlot(slot)) {
                        toughness += module.getToughness()[slot.getEntitySlotId()];
                    }
                }
            }
            return toughness;
        }
        return 0.0D;
    }

    public static double getSpeed(EquipmentSlot slot, CompoundTag tag) {
        if (tag == null) return 0.0;
        if (tag.isEmpty()) return 0.0;
        if (tag.contains(ModuleManager.LISTNAME)) {
            ListTag modules = tag.getList(ModuleManager.LISTNAME, ModuleManager.TYPE);
            double speed = 0.0D;
            if (ModuleManager.hasModule(modules, ModuleManager.SPEED)) {
                speed += (ModuleManager.getConfiguredValue(tag, ModuleManager.SPEED) + 1) * 0.20000000298023224D;
            }
            return speed;
        }
        return 0.0D;
    }

    public static double getDamage(EquipmentSlot slot, CompoundTag tag) {
        if (tag == null) return 0.0;
        if (tag.isEmpty()) return 0.0;
        if (tag.contains(ModuleManager.LISTNAME)) {
            ListTag modules = tag.getList(ModuleManager.LISTNAME, ModuleManager.TYPE);
            double damage = 0.0D;
            if (ModuleManager.hasModule(modules, ModuleManager.DAMAGE)) {
                damage += ModuleManager.getConfiguredValue(tag, ModuleManager.DAMAGE);
            }
            return damage;
        }
        return 0;
    }
}
