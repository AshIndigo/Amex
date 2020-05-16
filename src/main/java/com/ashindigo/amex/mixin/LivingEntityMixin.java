package com.ashindigo.amex.mixin;

import com.ashindigo.amex.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "computeFallDamage(FF)I", at = @At("RETURN"), cancellable = true)
    protected void calculateFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> info) {
        if (ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.FALL_RESIST)) {
            ModuleManager.takePowerWithCheck((LivingEntity) world.getEntityById(getEntityId()), ModuleManager.FALL_RESIST.powerUsage());
            info.setReturnValue(0);
            info.cancel();
        }
    }

    @Inject(method = "getJumpVelocity()F", at = @At("RETURN"), cancellable = true)
    protected void getJumpVelocity(CallbackInfoReturnable<Float> info) {
        if (ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.JUMP)) {
            info.setReturnValue(0.42F * this.getJumpVelocityMultiplier() + ((1 + ModuleManager.getConfiguredValue(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.JUMP)) *.1F)); // Config value is how many blocks higher the player can jump
            ModuleManager.takePowerWithCheck((LivingEntity) world.getEntityById(getEntityId()), ModuleManager.JUMP.powerUsage());
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
}
