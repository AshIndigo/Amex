package com.ashindigo.amex.mixin;

import com.ashindigo.amex.ModuleManager;
import com.ashindigo.amex.item.AmexArmor;
import com.ashindigo.amex.power.PowerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract void startFallFlying();

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "checkFallFlying()Z", at = @At("RETURN"), cancellable = true)
    protected void checkFlyFalling(CallbackInfoReturnable<Boolean> info) {
        if (!this.onGround && !this.isFallFlying() && !this.isTouchingWater()) {
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
            if (ModuleManager.hasModule(itemStack, ModuleManager.ELYTRA)) {
                startFallFlying();
                info.setReturnValue(true);
            }
        }
    }

    @Inject(method= "attack(Lnet/minecraft/entity/Entity;)V", at = @At("RETURN"))
    protected void attackLivingEntity(Entity attacked, CallbackInfo info) {
        if (attacked.isAttackable() && !attacked.handleAttack(world.getEntityById(getEntityId()))) {
            if (ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.CHEST), ModuleManager.DAMAGE)) {
                PowerManager.takePlayerPower((PlayerEntity) world.getEntityById(getEntityId()), ModuleManager.DAMAGE.powerUsage());
            }
        }
    }


}
