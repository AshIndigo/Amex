package com.ashindigo.amex.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class ResearchStationBlock extends BlockWithEntity {

    public ResearchStationBlock() {
        super(FabricBlockSettings.of(Material.METAL));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return null;
    }
}
