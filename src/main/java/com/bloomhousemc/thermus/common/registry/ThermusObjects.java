package com.bloomhousemc.thermus.common.registry;

import com.bloomhousemc.thermus.common.blocks.MachineFrameBlock;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlockEntity;
import com.bloomhousemc.thermus.common.blocks.pipe.PipeBlock;
import com.bloomhousemc.thermus.common.blocks.pipe.PipeBlockEntity;
import com.bloomhousemc.thermus.common.items.DebugThermusItem;
import com.bloomhousemc.thermus.common.items.HammerItem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;

import static com.bloomhousemc.thermus.common.utils.ThermusUtils.*;

public class ThermusObjects {

    //Debug
    public static final Item DEBUG_THERMUS = register("debug_thermus", new DebugThermusItem(gen()));


    //Items
    public static final Item COPPER_COIL = register("copper_coil", new Item(gen()));
    public static final Item IRON_COIL = register("iron_coil", new Item(gen()));
    public static final Item GOLD_COIL = register("gold_coil", new Item(gen()));
    public static final Item STEEL_COIL = register("steel_coil", new Item(gen()));

    public static final Item COPPER_PLATE = register("copper_plate", new Item(gen()));
    public static final Item IRON_PLATE = register("iron_plate", new Item(gen()));
    public static final Item GOLD_PLATE = register("gold_plate", new Item(gen()));
    public static final Item STEEL_PLATE = register("steel_plate", new Item(gen()));

    public static final Item COPPER_HAMMER = register("copper_hammer", new HammerItem(gen()));
    public static final Item IRON_HAMMER = register("iron_hammer", new HammerItem(gen()));
    public static final Item GOLD_HAMMER = register("gold_hammer", new HammerItem(gen()));
    public static final Item STEEL_HAMMER = register("steel_hammer", new HammerItem(gen()));
    public static final Item STEEL_INGOT = register("steel_ingot", new Item(gen()));



    //Blocks
    public static final Block BOILER_BLOCK = register("boiler_block", new BoilerBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0F).requiresTool().luminance(blockState -> blockState.get(ThermusPorperties.LIT) ? 15 : 0)), false);
    public static final Block PIPE_BLOCK = register("pipe_block", new PipeBlock(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK)), true);

    public static final Block STEEL_BLOCK = register("steel_block", new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)), true);
    public static final Block STEEL_SLAB = register("steel_slab", new SlabBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)), true);


    public static final Block MACHINE_FRAME = register("machine_frame", new MachineFrameBlock(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK)), true);

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
