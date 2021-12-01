package com.bloomhousemc.thermus.common.blocks.pipe;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.Pipe;

public class PipeBlock extends BlockWithEntity {
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");


    public PipeBlock(AbstractBlock.Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(this.getStateManager()
        .getDefaultState()
        .with(NORTH, false)
        .with(EAST, false)
        .with(SOUTH, false)
        .with(WEST, false)
        .with(UP, false)
        .with(DOWN, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PipeBlockEntity(pos,state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }
}
