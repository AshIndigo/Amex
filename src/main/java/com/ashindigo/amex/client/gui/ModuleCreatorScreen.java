package com.ashindigo.amex.client.gui;

import com.ashindigo.amex.screenhandler.ModuleCreatorScreenHandler;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TranslatableText;

public class ModuleCreatorScreen extends CottonInventoryScreen<ModuleCreatorScreenHandler> {

    public ModuleCreatorScreen(ModuleCreatorScreenHandler description, PlayerInventory playerInv) {
        super(description, playerInv.player, new TranslatableText("text.amex.module_creator"));
    }
}
