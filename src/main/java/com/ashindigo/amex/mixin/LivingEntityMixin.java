package com.ashindigo.amex.mixin;

import com.ashindigo.amex.ModuleManager;
import com.ashindigo.amex.power.PowerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        if (MathHelper.ceil((fallDistance - 3.0F) * damageMultiplier) > 0) {
            if (ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.FALL_RESIST) && PowerManager.getPlayerPower((PlayerEntity) world.getEntityById(getEntityId())) >= ModuleManager.FALL_RESIST.powerUsage()) {
                PowerManager.takePlayerPower((PlayerEntity) world.getEntityById(getEntityId()), ModuleManager.FALL_RESIST.powerUsage());
                info.setReturnValue(0);
                info.cancel();
            }
        }
    }

    @Inject(method = "getJumpVelocity()F", at = @At("RETURN"), cancellable = true)
    protected void getJumpVelocity(CallbackInfoReturnable<Float> info) {
        if (ModuleManager.hasModule(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.JUMP) && PowerManager.getPlayerPower((PlayerEntity) world.getEntityById(getEntityId())) >= ModuleManager.JUMP.powerUsage()) {
            info.setReturnValue(0.42F * this.getJumpVelocityMultiplier() + ((1 + ModuleManager.getConfiguredValue(this.getEquippedStack(EquipmentSlot.FEET), ModuleManager.JUMP)) * .1F)); // Config value is how many blocks higher the player can jump
            PowerManager.takePlayerPower((PlayerEntity) world.getEntityById(getEntityId()), ModuleManager.JUMP.powerUsage());
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
