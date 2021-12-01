package com.bloomhousemc.thermus;

import com.bloomhousemc.thermus.common.blocks.MachineFrameBlock;
import com.bloomhousemc.thermus.common.items.DebugThermusItem;
import com.bloomhousemc.thermus.common.items.HammerItem;
import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.FACING;

public class Thermus implements ModInitializer {
	public static final String MODID = "thermus";
	public static final ItemGroup THERMUS_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(ThermusObjects.STEEL_HAMMER));
	public static final Item DEBUG_THERMO = new DebugThermusItem(new FabricItemSettings().group(ItemGroup.MISC));


	@Override
	public void onInitialize() {
		ThermusObjects.init();


		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if(!world.isClient){
				if(player.getStackInHand(hand).getItem() instanceof HammerItem hammerItem){
					if(world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof MachineFrameBlock machineFrameBlock){
						Block topBlock = world.getBlockState(hitResult.getBlockPos().add(0,1,0)).getBlock();
						if (ThermusObjects.STEEL_BLOCK.equals(topBlock)) {

						}
						else if(ThermusObjects.STEEL_SLAB.equals(topBlock)){
							Direction direction = player.getHorizontalFacing();
							BlockState newBoiler = ThermusObjects.BOILER_BLOCK.getDefaultState().with(Properties.HORIZONTAL_FACING, direction);
							world.setBlockState(hitResult.getBlockPos(), newBoiler, 3);
							world.setBlockState(hitResult.getBlockPos().add(0,1,0), Blocks.AIR.getDefaultState());
							return ActionResult.CONSUME;
						}
						else{
							return ActionResult.FAIL;
						}
					}
				}
			}


			return ActionResult.PASS;
		});
	}
}
