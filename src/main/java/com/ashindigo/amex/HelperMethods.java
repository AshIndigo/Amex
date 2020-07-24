package com.ashindigo.amex;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import spinnery.widget.api.Position;

import javax.annotation.Nullable;
import java.util.Arrays;

public class HelperMethods {

    public static float[] fillArray(float val, int size) {
        float[] vals = new float[size];
        Arrays.fill(vals, val);
        return vals;
    }

    public static boolean positionsXYSame(Position pos1, Position pos2) {
        if (pos1.getX() == pos2.getX()) {
            return pos1.getY() == pos2.getY();
        }
        return false;
    }

    public static void setAttributeModifier(ItemStack stack, String name, EntityAttributeModifier modifier, EquipmentSlot slot) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("AttributeModifiers", 9)) {
            tag.put("AttributeModifiers", new ListTag());
        }

        ListTag listTag = tag.getList("AttributeModifiers", 10);
        CompoundTag compoundTag = modifier.toTag();
        compoundTag.putString("AttributeName", name);
        if (slot != null) {
            compoundTag.putString("Slot", slot.getName());
        }
        listTag.removeIf(tag1 -> ((CompoundTag) tag1).getString("AttributeName").equals(name));
        listTag.add(compoundTag);
        tag.put("AttributeModifiers", listTag);
    }
}
