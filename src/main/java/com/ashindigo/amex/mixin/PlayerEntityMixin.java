package com.ashindigo.amex.mixin;

import com.ashindigo.amex.ModuleManager;
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

    @Inject(method= "attackLivingEntity(Lnet/minecraft/entity/LivingEntity;)V", at = @At("RETURN"))
    protected void attackLivingEntity(LivingEntity entity, CallbackInfo info) {
        if (entity.isAttackable() && !entity.handleAttack(entity)) { // TODO What the fuck?
            if (ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.CHEST), ModuleManager.DAMAGE)) {
                ModuleManager.takePowerWithCheck((PlayerEntity) world.getEntityById(getEntityId()), ModuleManager.DAMAGE.powerUsage());
            }
        }
    }
}
