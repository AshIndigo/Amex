package com.ashindigo.amex.mixin;

import com.ashindigo.amex.AmexHelper;
import com.ashindigo.amex.ModuleManager;
import com.ashindigo.amex.item.AmexArmor;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};


    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "computeFallDamage(FF)I", at = @At("RETURN"), cancellable = true)
    protected void calculateFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> info) {
        if (ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.FALL_RESIST)) {
            ModuleManager.takePowerWithCheck((PlayerEntity) world.getEntityById(getEntityId()), ModuleManager.FALL_RESIST.powerUsage());
            info.setReturnValue(0);
            info.cancel();
        }
    }

    @Inject(method = "getJumpVelocity()F", at = @At("RETURN"), cancellable = true)
    protected void getJumpVelocity(CallbackInfoReturnable<Float> info) {
        if (ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.JUMP)) {
            info.setReturnValue(0.42F * this.getJumpVelocityMultiplier() + ((1 + ModuleManager.getConfiguredValue(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.JUMP)) * .1F)); // Config value is how many blocks higher the player can jump
            ModuleManager.takePowerWithCheck((PlayerEntity) world.getEntityById(getEntityId()), ModuleManager.JUMP.powerUsage());
            info.cancel();
        }
    }

    @Inject(method = "initAi()V", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z"), cancellable = true)
    protected void initAi(CallbackInfo info) {
        if (!this.world.isClient && ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.CHEST), ModuleManager.ELYTRA) && getFlag(7)) {
            this.setFlag(7, true);
            info.cancel();
        }
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void tick2(CallbackInfo ci, EquipmentSlot var3[], int var4, int var5, EquipmentSlot equipmentSlot, ItemStack itemStack3, ItemStack itemStack4) {
        System.out.println("Test");
        if (itemStack3.getItem() instanceof AmexArmor) {
            ((PlayerEntity) world.getEntityById(getEntityId())).getAttributes().removeAll(getAttributeModifiers(itemStack3, equipmentSlot, (PlayerEntity) world.getEntityById(getEntityId())));
        }
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;", ordinal = 1), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void tick(CallbackInfo ci, EquipmentSlot var3[], int var4, int var5, EquipmentSlot equipmentSlot, ItemStack itemStack3, ItemStack itemStack4) {
        System.out.println("Test");
        if (itemStack4.getItem() instanceof AmexArmor) {
            ((PlayerEntity) world.getEntityById(getEntityId())).getAttributes().replaceAll(getAttributeModifiers(itemStack4, equipmentSlot, (PlayerEntity) world.getEntityById(getEntityId())));
        }
    }

    private Multimap<String, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot, PlayerEntity player) {
        if (stack.hasTag() && slot.getType() == EquipmentSlot.Type.ARMOR && slot == ((AmexArmor) stack.getItem()).getSlotType()) {
            Multimap<String, EntityAttributeModifier> info2 = HashMultimap.create();
            info2.clear();
            if (ModuleManager.hasPower(player, ModuleManager.getArmorPowerNeed(player, slot))) {
                info2.put(EntityAttributes.ARMOR.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Armor modifier", AmexHelper.getProtection(slot, stack.getOrCreateTag()), EntityAttributeModifier.Operation.ADDITION));
                info2.put(EntityAttributes.ARMOR_TOUGHNESS.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Armor toughness", AmexHelper.getToughness(slot, stack.getOrCreateTag()), EntityAttributeModifier.Operation.ADDITION));
            }
            if (ModuleManager.hasPower(player, ModuleManager.SPEED.powerUsage()))
            info2.put(EntityAttributes.MOVEMENT_SPEED.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Movement Speed", AmexHelper.getSpeed(slot, stack.getOrCreateTag()), EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
            if (ModuleManager.hasPower(player, ModuleManager.DAMAGE.powerUsage()))
            info2.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Damage", AmexHelper.getDamage(slot, stack.getOrCreateTag()), EntityAttributeModifier.Operation.ADDITION));
            return info2;
        } else {
            Multimap<String, EntityAttributeModifier> info2 = HashMultimap.create();
            info2.clear();
            return info2;
        }
    }
}
