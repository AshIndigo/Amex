package com.ashindigo.amex;

import com.ashindigo.amex.upgradetable.UpgradeTableBlock;
import com.ashindigo.amex.upgradetable.UpgradeTableContainer;
import com.ashindigo.amex.upgradetable.UpgradeTableEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmexMod implements ModInitializer {
    public static final String MODID = "amex";
    public static BlockEntityType<?> UPGRADE_TABLE_ENTITY;
    public static ItemGroup AMEX_GROUP;
    // TODO - Code
    // Upgrade table, does... it actually need a container and block entity? - Gotta figure out how to just open up a screen with no need for a container
    // Module/Armor code
    // - More than one armor, at the cost a future weight system?
    // - Power system? Bug Vini for tech mod?
    // - Module could specify its power usage a tick
    // - Would need generator modules

    // TODO - Modules
    // Check - Z level issue - Vini needed? Maybe it doesn't respect z value upon initial draw?

    // TODO - Misc.
    // Lang - Update as more items as needed
    // Model files - Upgrade table should get custom model, or at least workbench like
    // Good comments
    // API Package? Or just make people use dev version of mod

    // TODO - Texture
    // Armor Texture
    // - Helm - Model
    // - Chestplate - Model
    // - Leggings - Icon and render
    // - Feet - Icon and render
    // Table Model/Texture

    // TODO - Model?
    // - Render on additional stuff if module is installed?


    @Override
    public void onInitialize() {
        UpgradeTableBlock upgradeTableBlock = new UpgradeTableBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5F).build());
        AMEX_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(upgradeTableBlock));
        ItemRegistry.addItems();
        Registry.register(Registry.BLOCK, new Identifier(MODID, "upgradetable"), upgradeTableBlock);
        Registry.register(Registry.ITEM, new Identifier(MODID, "upgradetable"), new BlockItem(upgradeTableBlock, new Item.Settings().group(AMEX_GROUP)));
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MODID, "upgradetable"), (syncId, id, player, buffer) -> new UpgradeTableContainer(syncId, player.inventory, buffer.readBlockPos(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
        UPGRADE_TABLE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "upgradetable"), BlockEntityType.Builder.create(UpgradeTableEntity::new, upgradeTableBlock).build(null));
        PacketRegistry.addPackets();
    }
}
