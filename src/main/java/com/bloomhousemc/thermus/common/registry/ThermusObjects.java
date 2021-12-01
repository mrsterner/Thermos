package com.bloomhousemc.thermus.common.registry;

import com.bloomhousemc.thermus.common.items.DebugThermusItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import static com.bloomhousemc.thermus.common.utils.ThermusUtils.*;

public class ThermusObjects {
    public static final Item DEBUG_THERMUS = new DebugThermusItem(gen());



    public static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
        BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
        FLUIDS.keySet().forEach(fluid -> Registry.register(Registry.FLUID, FLUIDS.get(fluid), fluid));
        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
    }
}
