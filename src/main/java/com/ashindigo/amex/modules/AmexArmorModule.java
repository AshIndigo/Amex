package com.ashindigo.amex.modules;

import com.ashindigo.amex.AmexHelper;
import com.ashindigo.amex.HelperMethods;
import com.ashindigo.amex.ModuleManager;
import com.ashindigo.amex.power.PowerManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.IntSupplier;

import static com.ashindigo.amex.ModuleManager.MODIFIERS;

public class AmexArmorModule extends AmexModule {
    private final float[] protection;
    private final float[] toughness;

    public AmexArmorModule(Identifier name, ItemStack itemStack, float[] protection, float[] toughness, EquipmentSlot[] equipmentSlots, boolean config, IntSupplier power) {
        super(name, itemStack, equipmentSlots, config, power);
        this.protection = protection;
        this.toughness = toughness;
    }

    @Override
    public void onTick(ItemStack stack, PlayerEntity entity) {
        if (PowerManager.getPlayerPower(entity) > powerUsage()) {
            HelperMethods.setAttributeModifier(stack, EntityAttributes.GENERIC_ARMOR.getTranslationKey(), new EntityAttributeModifier(MODIFIERS[((ArmorItem) stack.getItem()).getSlotType().getEntitySlotId()], "Armor modifier", AmexHelper.getProtection(((ArmorItem) stack.getItem()).getSlotType(), stack), EntityAttributeModifier.Operation.ADDITION), ((ArmorItem) stack.getItem()).getSlotType());
            HelperMethods.setAttributeModifier(stack, EntityAttributes.GENERIC_ARMOR_TOUGHNESS.getTranslationKey(), new EntityAttributeModifier(MODIFIERS[((ArmorItem) stack.getItem()).getSlotType().getEntitySlotId()], "Armor toughness", AmexHelper.getToughness(((ArmorItem) stack.getItem()).getSlotType(), stack.getOrCreateTag()), EntityAttributeModifier.Operation.ADDITION), ((ArmorItem) stack.getItem()).getSlotType());
            PowerManager.takePlayerPower(entity, powerUsage());
        } else {
            HelperMethods.setAttributeModifier(stack, EntityAttributes.GENERIC_ARMOR.getTranslationKey(), new EntityAttributeModifier(MODIFIERS[((ArmorItem) stack.getItem()).getSlotType().getEntitySlotId()], "Armor modifier", 0, EntityAttributeModifier.Operation.ADDITION), ((ArmorItem) stack.getItem()).getSlotType());
            HelperMethods.setAttributeModifier(stack, EntityAttributes.GENERIC_ARMOR_TOUGHNESS.getTranslationKey(), new EntityAttributeModifier(MODIFIERS[((ArmorItem) stack.getItem()).getSlotType().getEntitySlotId()], "Armor toughness",0, EntityAttributeModifier.Operation.ADDITION), ((ArmorItem) stack.getItem()).getSlotType());
        }
    }

    public float[] getProtection() {
        return protection;
    }

    public float[] getToughness() {
        return toughness;
    }
}
