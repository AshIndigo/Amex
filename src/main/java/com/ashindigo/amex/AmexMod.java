package com.ashindigo.amex;

import com.ashindigo.amex.upgradetable.UpgradeTableBlock;
import com.ashindigo.amex.upgradetable.UpgradeTableContainer;
import com.ashindigo.amex.upgradetable.UpgradeTableEntity;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import spinnery.common.container.BaseContainer;

public class AmexMod implements ModInitializer {
    public static final String MODID = "amex";
    public static BlockEntityType<?> UPGRADE_TABLE_ENTITY;
    public static ItemGroup AMEX_GROUP;
    public static AmexConfig config;
    public static ScreenHandlerType<? extends BaseContainer> tableScreenHandler;
    // TODO - Code
    // Upgrade table, does... it actually need a container and block entity? - Gotta figure out how to just open up a screen with no need for a container
    // Concern for certain modules, certain values may not be reset upon armor removal i.e autostep

    // TODO - Modules
    // Check - Z level issue - Vini needed? Maybe it doesn't respect z value upon initial draw?
    // Getting fall pads from uninstalling solar? What?
    // Positioning code is all fucked up at the moment, tabs arent acting right

    // TODO - Misc.
    // Lang - Update as more items as needed
    // Model files - Upgrade table should get custom model, or at least workbench like
    // Good comments
    // API Package? Or just make people use dev version of mod

    // TODO - Texture
    // Armor Texture
    // - Helm - Model
    // - Chestplate - Model
    // - Leggings - render
    // - Feet - render
    // Table Model/Texture

    // TODO - Model?
    // - Render on additional stuff if module is installed?


    @Override
    public void onInitialize() {
        AutoConfig.register(AmexConfig.class, GsonConfigSerializer::new);
        UpgradeTableBlock upgradeTableBlock = new UpgradeTableBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5F));
        AMEX_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(upgradeTableBlock));
        ItemRegistry.addItems();
        Registry.register(Registry.BLOCK, new Identifier(MODID, "upgradetable"), upgradeTableBlock);
        Registry.register(Registry.ITEM, new Identifier(MODID, "upgradetable"), new BlockItem(upgradeTableBlock, new Item.Settings().group(AMEX_GROUP)));
        tableScreenHandler = ScreenHandlerRegistry.registerSimple(new Identifier(AmexMod.MODID, "upgradetable"), UpgradeTableContainer::new);
        //ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MODID, "upgradetable"), (syncId, id, player, buffer) -> new UpgradeTableContainer(syncId, player.inventory, buffer.readBlockPos(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
        UPGRADE_TABLE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "upgradetable"), BlockEntityType.Builder.create(UpgradeTableEntity::new, upgradeTableBlock).build(null));
        PacketRegistry.addPackets();
        config = AutoConfig.getConfigHolder(AmexConfig.class).getConfig();
    }
}
