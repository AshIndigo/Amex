package com.ashindigo.amex.api;

import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public interface ArmorModule {

    int getComplexity();

    default Text getName(ItemStack stack) {
        return stack.getName();
    }

    default Text getDescription() {
        return new LiteralText("");
    }
}
