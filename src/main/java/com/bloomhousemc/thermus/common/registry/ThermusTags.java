package com.bloomhousemc.thermus.common.registry;

import com.bloomhousemc.thermus.Thermus;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.apache.commons.io.input.TaggedReader;

public class ThermusTags {
    public static final Tag<Item> COIL = TagFactory.ITEM.create(new Identifier(Thermus.MODID, "coil"));
}
