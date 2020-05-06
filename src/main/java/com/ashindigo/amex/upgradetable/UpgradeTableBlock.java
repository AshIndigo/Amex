package com.ashindigo.amex.upgradetable;

import com.ashindigo.amex.AmexMod;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class UpgradeTableBlock extends BlockWithEntity {

    public UpgradeTableBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new UpgradeTableEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(AmexMod.MODID, "upgradetable"), player, (buffer) -> { buffer.writeBlockPos(pos); buffer.writeInt(0); buffer.writeInt(0); buffer.writeInt(0); buffer.writeText(new TranslatableText(this.getTranslationKey())); });
        }
        return ActionResult.SUCCESS;
    }

    @Override
    @Deprecated
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

}
