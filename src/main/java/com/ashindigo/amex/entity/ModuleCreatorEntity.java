package com.ashindigo.amex.entity;

import com.ashindigo.amex.Amex;
import com.ashindigo.amex.screenhandler.ModuleCreatorScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

// TODO Does this even need a BE? It might be possible to just make it a GUI/container aka workbench
public class ModuleCreatorEntity extends BlockEntity implements NamedScreenHandlerFactory {

    public ModuleCreatorEntity() {
        super(Amex.MODULE_CREATOR_TYPE);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ModuleCreatorScreenHandler(syncId, inv);
    }
}
