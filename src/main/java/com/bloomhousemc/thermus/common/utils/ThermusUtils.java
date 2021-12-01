package com.bloomhousemc.thermus.common.utils;

import com.bloomhousemc.thermus.Thermus;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class ThermusUtils {

    public static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
    public static final Map<FlowableFluid, Identifier> FLUIDS = new LinkedHashMap<>();
    public static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

    public static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        BLOCK_ENTITY_TYPES.put(type, new Identifier(Thermus.MODID, id));
        return type;
    }

    public static <T extends Block> T register(String name, T block, boolean createItem) {
        BLOCKS.put(block, new Identifier(Thermus.MODID, name));
        if (createItem) {
            ITEMS.put(new BlockItem(block, gen()), BLOCKS.get(block));
        }
        return block;
    }

    public static <T extends Item> T register(String name, T item) {
        ITEMS.put(item, new Identifier(Thermus.MODID, name));
        return item;
    }

    public static Item.Settings gen() {
        return new Item.Settings().group(Thermus.THERMUS_GROUP);
    }
}
