package com.bloomhousemc.thermus.common.registry;

import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlockEntity;
import com.bloomhousemc.thermus.common.blocks.pipe.PipeBlock;
import com.bloomhousemc.thermus.common.blocks.pipe.PipeBlockEntity;
import com.bloomhousemc.thermus.common.items.DebugThermusItem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import static com.bloomhousemc.thermus.common.utils.ThermusUtils.*;

public class ThermusObjects {

    //Debug
    public static final Item DEBUG_THERMUS = register("debug_thermus", new DebugThermusItem(gen()));


    //Items
    public static final Item COPPER_COIL = register("copper_coil", new Item(gen()));
    public static final Item IRON_COIL = register("iron_coil", new Item(gen()));
    public static final Item GOLD_COIL = register("gold_coil", new Item(gen()));
    public static final Item STEEL_COIL = register("steel_coil", new Item(gen()));



    //Blocks
    public static final Block BOILER_BLOCK = register("boiler_block", new BoilerBlock(FabricBlockSettings.copyOf(Blocks.STONE)), true);
    public static final Block PIPE_BLOCK = register("pipe_block", new PipeBlock(FabricBlockSettings.copyOf(Blocks.STONE)), true);

    // Blocketities
    public static final BlockEntityType<BoilerBlockEntity> BOILER_BLOCK_ENTITY = register("boiler_block_entity", FabricBlockEntityTypeBuilder.create(BoilerBlockEntity::new, BOILER_BLOCK).build(null));
    public static final BlockEntityType<PipeBlockEntity> PIPE_BLOCK_ENTITY = register("pipe_block_entity", FabricBlockEntityTypeBuilder.create(PipeBlockEntity::new, PIPE_BLOCK).build(null));



    public static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
        BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
        FLUIDS.keySet().forEach(fluid -> Registry.register(Registry.FLUID, FLUIDS.get(fluid), fluid));
        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
    }
}
