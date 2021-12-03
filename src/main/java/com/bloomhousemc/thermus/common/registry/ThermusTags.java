package com.bloomhousemc.thermus.common.registry;

import com.bloomhousemc.thermus.Thermus;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.apache.commons.io.input.TaggedReader;

public class ThermusTags {
    public static final Tag<Item> COIL = TagFactory.ITEM.create(new Identifier(Thermus.MODID, "coil"));

    public static final Tag<Item> METAL_CLOTHING = TagFactory.ITEM.create(new Identifier(Thermus.MODID, "metal_clothing"));
    public static final Tag<Item> LEATHER_CLOTHING = TagFactory.ITEM.create(new Identifier(Thermus.MODID, "leather_clothing"));
    public static final Tag<Item> INSULATED_CLOTHING_TIER_1 = TagFactory.ITEM.create(new Identifier(Thermus.MODID, "insulated_clothing_tier_1"));
    public static final Tag<Item> INSULATED_CLOTHING_TIER_2 = TagFactory.ITEM.create(new Identifier(Thermus.MODID, "insulated_clothing_tier_2"));
    public static final Tag<Item> INSULATED_CLOTHING_TIER_3 = TagFactory.ITEM.create(new Identifier(Thermus.MODID, "insulated_clothing_tier_3"));

    public static final Tag<Block> TEMPERATURE_HOLDER = TagFactory.BLOCK.create(new Identifier(Thermus.MODID, "temperature_holder"));
}
