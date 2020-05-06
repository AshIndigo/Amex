package com.ashindigo.amex.item;

import com.ashindigo.amex.BlankArmorMaterial;
import com.ashindigo.amex.ModuleManager;
import com.ashindigo.amex.modules.AmexModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
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
            module.onTick(stack, entity);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (((AmexArmor) stack.getItem()).slot == EquipmentSlot.FEET) {
            tooltip.add(new LiteralText("+" + ModuleManager.getConfiguredValue(stack, ModuleManager.JUMP) + " ").append(new TranslatableText("text.amex.jump_boost")).setStyle(new Style().setColor(Formatting.BLUE)));
        }
    }

}
