package com.bloomhousemc.thermus.common.blocks.boiler;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import com.bloomhousemc.thermus.common.registry.ThermusTags;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BoilerBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty LIT = Properties.LIT;
    public static final IntProperty COAL = IntProperty.of("coal", 0,4);
    public static final IntProperty COIL = IntProperty.of("coil", 0,4);
    public BoilerBlock(Settings settings) {
        super(settings.nonOpaque().luminance((state) -> (state.get(LIT) ? 10 : 0)));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(COIL,0).with(LIT, false).with(COAL, 0));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (tickerWorld, pos, tickerState, blockEntity) -> BoilerBlockEntity.tick(tickerWorld, pos, tickerState, (BoilerBlockEntity) blockEntity);

    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BoilerBlockEntity(pos,state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(COIL).add(LIT).add(COAL);
    }

    public static void updateCoal(BlockState state, World world, BlockPos pos){
        if(world.getBlockEntity(pos) instanceof BoilerBlockEntity boilerBlockEntity){
            int count = boilerBlockEntity.getStack(0).getCount() + boilerBlockEntity.getStack(1).getCount();
            world.setBlockState(pos, state.with(COAL, count == 0 ? 0 : count < 16 ? 1 : count < 32 ? 2 : count < 48 ? 3 : 4));
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient){
            ItemStack itemStack = player.getStackInHand(hand);
            if(state.get(COIL) == 0 && ThermusTags.COIL.contains(itemStack.getItem())){
                world.setBlockState(pos, state.with(COIL, itemStack.isOf(ThermusObjects.COPPER_COIL) ? 1 : itemStack.isOf(ThermusObjects.GOLD_COIL) ? 2 : itemStack.isOf(ThermusObjects.IRON_COIL) ? 3 : 4));

            }
            if(world.getBlockEntity(pos) instanceof BoilerBlockEntity boilerBlockEntity && itemStack.isOf(Items.FLINT_AND_STEEL) && (boilerBlockEntity.getStack(0).getCount()+boilerBlockEntity.getStack(1).getCount()) > 0){
                world.setBlockState(pos,state.with(LIT, true));
            }
            if(world.getBlockEntity(pos) instanceof BoilerBlockEntity boilerBlockEntity && (itemStack.isOf(Items.COAL) || itemStack.isOf(Items.CHARCOAL))){
                if((boilerBlockEntity.getStack(0).getCount() + boilerBlockEntity.getStack(1).getCount())<64){
                    if(boilerBlockEntity.getStack(itemStack.isOf(Items.COAL) ? 0 : 1).isOf(Items.AIR)){
                        boilerBlockEntity.setStack(itemStack.isOf(Items.COAL) ? 0 : 1, itemStack.isOf(Items.COAL) ? new ItemStack(Items.COAL) : new ItemStack(Items.CHARCOAL));
                        player.getStackInHand(hand).decrement(1);
                        updateCoal(state,world,pos);
                        boilerBlockEntity.markDirty();
                        return ActionResult.CONSUME;
                    }
                    if(player.isSneaking()){
                        while((boilerBlockEntity.getStack(0).getCount() + boilerBlockEntity.getStack(1).getCount())<64 && player.getStackInHand(hand).getCount()>0){
                            if(boilerBlockEntity.getStack(itemStack.isOf(Items.COAL) ? 0 : 1).isOf(Items.AIR)){
                                boilerBlockEntity.setStack(itemStack.isOf(Items.COAL) ? 0 : 1, itemStack.isOf(Items.COAL) ? new ItemStack(Items.COAL) : new ItemStack(Items.CHARCOAL));
                                player.getStackInHand(hand).decrement(1);
                            }
                            boilerBlockEntity.getStack(itemStack.isOf(Items.COAL) ? 0 : 1).increment(1);
                            player.getStackInHand(hand).decrement(1);
                        }
                    }else{
                        boilerBlockEntity.getStack(itemStack.isOf(Items.COAL) ? 0 : 1).increment(1);
                        player.getStackInHand(hand).decrement(1);
                    }
                }
                updateCoal(state,world,pos);
                boilerBlockEntity.markDirty();
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BoilerBlockEntity boilerBlockEntity) {
                ItemScatterer.spawn(world, pos, boilerBlockEntity);
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing()).with(COIL,0).with(LIT, false).with(COAL, 0);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING))).with(COIL,0);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING))).with(COIL,0);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

}
