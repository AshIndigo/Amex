package com.ashindigo.amex.upgradetable;

import com.ashindigo.amex.AmexMod;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import spinnery.common.container.BaseContainer;

public class UpgradeTableContainer extends BaseContainer {
    public UpgradeTableContainer(int synchronizationID, PlayerInventory playerInventory) {
        super(synchronizationID, playerInventory);
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return AmexMod.tableScreenHandler;
    }
}
