package com.bloomhousemc.thermus;

import com.bloomhousemc.thermus.common.blocks.MachineFrameBlock;
import com.bloomhousemc.thermus.common.items.DebugThermusItem;
import com.bloomhousemc.thermus.common.items.HammerItem;
import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.system.CallbackI;

import static net.minecraft.block.SlabBlock.TYPE;

public class Thermus implements ModInitializer {
	public static final String MODID = "thermus";
	public static final ItemGroup THERMUS_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(ThermusObjects.STEEL_HAMMER));
	public static final Item DEBUG_THERMO = new DebugThermusItem(new FabricItemSettings().group(ItemGroup.MISC));

	public static ThermusConfig config;
	@Override
	public void onInitialize() {
		AutoConfig.register(ThermusConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ThermusConfig.class).getConfig();
		ThermusObjects.init();




		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if(!world.isClient){
				if(player.getStackInHand(hand).getItem() instanceof HammerItem hammerItem){
					if(world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof MachineFrameBlock machineFrameBlock){
						Block topBlock = world.getBlockState(hitResult.getBlockPos().add(0,1,0)).getBlock();
						if (ThermusObjects.STEEL_BLOCK.equals(topBlock)) {

						}
						else if(ThermusObjects.STEEL_SLAB.equals(topBlock)){
							BlockPos blockPos = hitResult.getBlockPos();
							if(world.getBlockState(hitResult.getBlockPos().add(0,1,0)).get(TYPE) == SlabType.DOUBLE){
								world.spawnEntity(new ItemEntity(world, blockPos.getX(),blockPos.getY()+0.5F,blockPos.getZ(), new ItemStack(ThermusObjects.STEEL_SLAB)));
							}
							Direction direction = player.getHorizontalFacing();
							BlockState newBoiler = ThermusObjects.BOILER_BLOCK.getDefaultState().with(Properties.HORIZONTAL_FACING, direction);
							world.breakBlock(blockPos, false);
							world.breakBlock(blockPos.add(0,1,0), false);
							world.setBlockState(blockPos, newBoiler, 3);
							world.setBlockState(blockPos.add(0,1,0), Blocks.AIR.getDefaultState());
							double Width = 1.0;
							world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, newBoiler), blockPos.getX() + ((double)world.random.nextFloat() - 0.5D) * Width, blockPos.getY() + 0.1D, blockPos.getZ() + ((double)world.random.nextFloat() - 0.5D) * Width, 4.0D * ((double)world.random.nextFloat() - 0.5D), 0.5D, ((double)world.random.nextFloat() - 0.5D) * 4.0D);
							world.playSound(null, blockPos, SoundEvents.BLOCK_COPPER_BREAK, SoundCategory.BLOCKS, 1F, 1F);
							player.getStackInHand(hand).damage(1, world.random, (ServerPlayerEntity)player);
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
