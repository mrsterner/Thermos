package com.bloomhousemc.thermus.common.blocks.boiler;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import com.bloomhousemc.thermus.common.registry.ThermusPorperties;
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
    public BoilerBlock(Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(ThermusPorperties.COIL,0).with(ThermusPorperties.LIT, false).with(ThermusPorperties.COAL, 0));
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
        builder.add(FACING).add(ThermusPorperties.COIL).add(ThermusPorperties.LIT).add(ThermusPorperties.COAL);
    }



    public static void setZeroState(World world, BlockPos pos, BlockState state){
        if(world.getBlockEntity(pos) instanceof BoilerBlockEntity boilerBlockEntity){
            world.setBlockState(pos,state.with(ThermusPorperties.LIT, false));
            world.setBlockState(pos,state.with(ThermusPorperties.COAL, 0));
            boilerBlockEntity.markDirty();
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        boolean client = world.isClient;
        if (!client) {
            ((BoilerBlockEntity)world.getBlockEntity(pos)).onUse(world,state,pos,player,hand);
        }
        return ActionResult.success(client);
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
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing()).with(ThermusPorperties.COIL,0).with(ThermusPorperties.LIT, false).with(ThermusPorperties.COAL, 0);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING))).with(ThermusPorperties.COIL,0);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING))).with(ThermusPorperties.COIL,0);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

}
