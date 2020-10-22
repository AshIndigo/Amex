package com.ashindigo.amex.screenhandler;

import com.ashindigo.amex.Amex;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import net.minecraft.entity.player.PlayerInventory;

public class ModuleCreatorScreenHandler extends SyncedGuiDescription {

    public ModuleCreatorScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(Amex.MODULE_CREATOR_HANDLER, syncId, playerInventory);
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 200);

        root.add(this.createPlayerInventoryPanel(), 0, 3);

        root.validate(this);
    }
}
