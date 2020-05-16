package com.ashindigo.amex.modules;

import com.ashindigo.amex.ModuleManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.IntSupplier;

public class AmexGeneratorModule extends AmexModule {
    private IntSupplier powerGen;

    /**
     * Defines a normal module
     *
     * @param name           The modules internal name
     * @param itemStack      The component used to install the module, can be any item stack
     * @param equipmentSlots Valid armor slots for the module
     * @param config         Can this module be configured
     * @param powerGen          Power generated
     */
    public AmexGeneratorModule(Identifier name, ItemStack itemStack, EquipmentSlot[] equipmentSlots, boolean config, IntSupplier powerGen) {
        super(name, itemStack, equipmentSlots, config, () -> 0);
        this.powerGen = powerGen;
    }

    @Override
    public void onTick(ItemStack stack, LivingEntity entity) {
        ModuleManager.addPowerWithCheck(entity, powerGen.getAsInt());
    }

    public int getPowerGen() {
        return powerGen.getAsInt();
    }
}
