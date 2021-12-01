package com.bloomhousemc.thermus.common.utils;

import com.bloomhousemc.thermus.Thermus;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.gen.random.ChunkRandom;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.gen.random.ChunkRandom;

public class ThermusUtils {

    protected static final OctaveSimplexNoiseSampler TEMPERATURE_NOISE = new OctaveSimplexNoiseSampler(new ChunkRandom(new ThermusRandom()), ImmutableList.of(0));

    public static double simpleTimeOfDay(World world){
        return (double) world.getTimeOfDay()/10;
    }

    /**Periodic function of cosine with modification, the smoothness of the curve is determined by b and will have more distinct nighttime/daytime temperatures
     * @Pre wobble > 0
     * @param time of day
     * @param curvature is the cos wave smoothness, 0 for max smoothness and 10 for almost max hardness
     * @param wobble determines the fluctuation in temp regardless of time, default 2, 30 is almost no fluctuation
     * @param minMax min and max temp
     * @param offset offsets the minMax, if {@code minMax} is 10 and {@code offset} is 5, output temperature will be 15°C and -5°C
     * @return temperature
     */
    public static int dayTimeTemperatureFunction(double time, int curvature, int wobble, int minMax, int offset){
        var cosPeriod = Math.cos((Math.PI * time / 1200) - Math.PI / 2);
        double T = Math.sqrt((1+curvature*curvature)/(1+curvature*curvature* cosPeriod * cosPeriod))* cosPeriod*minMax+offset;
        double sine = Math.sin(wobble*time)/(wobble+2);
        return (int)(T - sine);
    }







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
