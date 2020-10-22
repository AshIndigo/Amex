package com.ashindigo.amex;

import com.ashindigo.amex.entity.ModuleCreatorEntity;
import com.ashindigo.amex.screenhandler.ModuleCreatorScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Amex implements ModInitializer {

    public static final String MODID = "amex";
    public static final ItemGroup AMEX_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(Items.IRON_CHESTPLATE));
    public static BlockEntityType<?> MODULE_CREATOR_TYPE;
    public static ScreenHandlerType<?> MODULE_CREATOR_HANDLER;

    @Override
    public void onInitialize() {
        AmexItems.registerItems();
        AmexBlocks.registerBlocks();
        MODULE_CREATOR_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "module_creator"), BlockEntityType.Builder.create(ModuleCreatorEntity::new, AmexBlocks.module_creator).build(null));
        MODULE_CREATOR_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MODID, "module_creator"), ModuleCreatorScreenHandler::new);
    }
}
