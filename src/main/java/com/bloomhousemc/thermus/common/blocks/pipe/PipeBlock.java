package com.bloomhousemc.thermus.common.blocks.pipe;

import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.FACING;

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

    public static final Map<Direction, BooleanProperty> PROP_MAP = Util.make(new HashMap<>(),
    map -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
        map.put(Direction.UP, UP);
        map.put(Direction.DOWN, DOWN);
    });

    private BlockState getOppositeConnection(World world, BlockPos pos) {
        Boolean north = canConnect(world, pos.north(), Direction.SOUTH);
        Boolean east = canConnect(world, pos.east(), Direction.WEST);
        Boolean south = canConnect(world, pos.south(), Direction.NORTH);
        Boolean west = canConnect(world, pos.west(), Direction.EAST);
        Boolean up = canConnect(world, pos.up(), Direction.DOWN);
        Boolean down = canConnect(world, pos.down(), Direction.UP);

        return this.getDefaultState()
        .with(NORTH, north)
        .with(EAST, east)
        .with(SOUTH, south)
        .with(WEST, west)
        .with(UP, up)
        .with(DOWN, down);
    }

    private static final float INSET = 0.3125F;
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final List<VoxelShape> connections = new ArrayList<>();
        final VoxelShape baseShape = VoxelShapes.cuboid(INSET, INSET, INSET, 1 - INSET, 1 - INSET, 1 - INSET);
        for (Direction dir : Direction.values()) {
            if (state.get(PipeBlock.PROP_MAP.get(dir))) {
                double[] mins = { INSET, INSET, INSET };
                double[] maxs = { 1 - INSET, 1 - INSET, 1 - INSET };
                int axis = dir.getAxis().ordinal();
                if (dir.getDirection() == Direction.AxisDirection.POSITIVE) {
                    maxs[axis] = 1;
                } else {
                    mins[axis] = 0;
                }
                connections.add(VoxelShapes.cuboid(mins[0], mins[1], mins[2], maxs[0], maxs[1], maxs[2]));
            }
        }
        return VoxelShapes.union(baseShape, connections.toArray(new VoxelShape[] { }));
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

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getOppositeConnection(context.getWorld(), context.getBlockPos());
    }

    public Property<Boolean> getProperty(Direction facing) {
        return PROP_MAP.get(facing);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
        if(!world.isClient()) {
            Boolean value = canConnect(world, posFrom, direction.getOpposite());
            return state.with(getProperty(direction), value);
        }
        return state;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
    }

    private boolean canConnect(WorldAccess worldAccess, BlockPos blockPos, Direction direction){
        Block block = worldAccess.getBlockState(blockPos).getBlock();
        if(block instanceof BoilerBlock boilerBlock){
            Direction targetDirection = worldAccess.getBlockState(blockPos).get(FACING);
            if(targetDirection == Direction.SOUTH || targetDirection == Direction.NORTH){
                return direction == Direction.EAST || direction == Direction.WEST;
            }
            if(targetDirection == Direction.EAST || targetDirection == Direction.WEST){
                return direction == Direction.SOUTH || direction == Direction.NORTH;
            }
        }
        return block instanceof PipeBlock;
    }
}
