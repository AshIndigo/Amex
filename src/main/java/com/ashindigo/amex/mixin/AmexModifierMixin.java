package com.ashindigo.amex.mixin;

import com.ashindigo.amex.AmexHelper;
import com.ashindigo.amex.item.AmexArmor;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class AmexModifierMixin {

	private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

	@Shadow public abstract boolean hasTag();

	@Shadow public abstract Item getItem();

	@Shadow @Nullable public abstract CompoundTag getTag();

	@Inject(method = "getAttributeModifiers", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	protected void onGetAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> info) {
			if (this.getItem() instanceof AmexArmor && hasTag() && slot.getType() == EquipmentSlot.Type.ARMOR && slot == ((AmexArmor) this.getItem()).getSlotType()) {
				Multimap<String, EntityAttributeModifier>  info2 = info.getReturnValue();
				info2.clear();
				info2.put(EntityAttributes.ARMOR.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Armor modifier", AmexHelper.getProtection(slot, getTag()), EntityAttributeModifier.Operation.ADDITION));
				info2.put(EntityAttributes.ARMOR_TOUGHNESS.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Armor toughness", AmexHelper.getToughness(slot, getTag()), EntityAttributeModifier.Operation.ADDITION));
				info2.put(EntityAttributes.MOVEMENT_SPEED.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Movement Speed", AmexHelper.getSpeed(slot, getTag()), EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
				info2.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Damage", AmexHelper.getDamage(slot, getTag()), EntityAttributeModifier.Operation.ADDITION));
				info.setReturnValue(info2);
			} else {
				Multimap<String, EntityAttributeModifier>  info2 = info.getReturnValue();
				info2.clear();
				info.setReturnValue(info2);
			}
	}
}
