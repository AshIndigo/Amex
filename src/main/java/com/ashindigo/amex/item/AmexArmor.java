package com.ashindigo.amex.item;

import com.ashindigo.amex.AmexConfig;
import com.ashindigo.amex.AmexMod;
import com.ashindigo.amex.BlankArmorMaterial;
import com.ashindigo.amex.ModuleManager;
import com.ashindigo.amex.modules.AmexModule;
import com.ashindigo.amex.power.PowerManager;
import com.sun.org.apache.xpath.internal.operations.Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class AmexArmor extends ArmorItem {
    public AmexArmor(EquipmentSlot slot, Item.Settings settings) {
        super(new BlankArmorMaterial(), slot, settings);
    }

    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        for (AmexModule module : ModuleManager.getModulesOnArmor(stack)) {
            if (entity instanceof PlayerEntity) {
                if (((PlayerEntity) entity).getEquippedStack(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, slot)).equals(stack))
                    module.onTick(stack, (PlayerEntity) entity);
            }
        }

    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText("Energy: " + PowerManager.getPower(stack) + "/" + AmexMod.config.maxPower));
        if (((AmexArmor) stack.getItem()).slot == EquipmentSlot.FEET) {
            tooltip.add(new LiteralText("+" + ModuleManager.getConfiguredValue(stack, ModuleManager.JUMP) + " ").append(new TranslatableText("text.amex.jump_boost")).setStyle(new Style().setColor(Formatting.BLUE)));
        }
    }

}
