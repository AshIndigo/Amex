package com.ashindigo.amex.upgradetable;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.common.BaseScreenHandler;

public class UpgradeTableContainer extends BaseScreenHandler {
    public UpgradeTableContainer(int synchronizationID, PlayerInventory linkedPlayerInventory, BlockPos blockPos, int x, int y, int z) {
        super(synchronizationID, linkedPlayerInventory);
    }
}
