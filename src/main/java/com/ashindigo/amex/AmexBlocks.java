package com.ashindigo.amex;

import com.ashindigo.amex.block.ModuleCreatorBlock;
import com.ashindigo.amex.block.ResearchStationBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmexBlocks {

    public static ResearchStationBlock research_station;
    public static ModuleCreatorBlock module_creator;

    public static void registerBlocks() {
        research_station = registerBlock("research_station", new ResearchStationBlock());
        module_creator = registerBlock("module_creator", new ModuleCreatorBlock());

    }

    private static <T extends Block> T registerBlock(String name, T block) {
        Registry.register(Registry.BLOCK, new Identifier(Amex.MODID, name), block);
        Registry.register(Registry.ITEM, new Identifier(Amex.MODID, name), new BlockItem(block, new Item.Settings().group(Amex.AMEX_GROUP)));
        return block;
    }
}
