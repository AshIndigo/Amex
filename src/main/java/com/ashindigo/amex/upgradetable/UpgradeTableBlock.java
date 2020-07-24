package com.ashindigo.amex.upgradetable;

import com.ashindigo.amex.AmexMod;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
            player.openHandledScreen(new NamedScreenHandlerFactory(){
                @Override
                public Text getDisplayName() {
                    return new TranslatableText("block.amex.upgradetable");
                }

                @Nullable
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return AmexMod.tableScreenHandler.create(syncId, inv);
                }
            });
            //ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(AmexMod.MODID, "upgradetable"), player, (buffer) -> { buffer.writeBlockPos(pos); buffer.writeInt(0); buffer.writeInt(0); buffer.writeInt(0); buffer.writeText(new TranslatableText(this.getTranslationKey())); });
        }
        return ActionResult.SUCCESS;
    }

    @Override
    @Deprecated
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

}
