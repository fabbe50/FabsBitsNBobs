package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.world.block.base.ExtFaceAttachedHorizontalDirectionalBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PoweredThinLightBlock extends ExtFaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<PoweredThinLightBlock> CODEC = simpleCodec(PoweredThinLightBlock::new);

    protected static final VoxelShape CEILING_AABB = Block.box(5.0F, 15.0F, 5.0F, 11.0F, 16.0F, 11.0F);
    protected static final VoxelShape FLOOR_AABB = Block.box(5.0F, 0.0F, 5.0F, 11.0F, 1.0F, 11.0F);
    protected static final VoxelShape SOUTH_AABB = Block.box(5.0F, 5.0F, 0.0F, 11.0F, 11.0F, 1.0F);
    protected static final VoxelShape NORTH_AABB = Block.box(5.0F, 5.0F, 15.0F, 11.0F, 11.0F, 16.0F);
    protected static final VoxelShape EAST_AABB = Block.box(0.0F, 5.0F, 5.0F, 1.0F, 11.0F, 11.0F);
    protected static final VoxelShape WEST_AABB = Block.box(15.0F, 5.0F, 5.0F, 16.0F, 11.0F, 11.0F);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PoweredThinLightBlock(Properties properties) {
        super(properties.strength(0.3F).sound(SoundType.GLASS).isValidSpawn((blockState, blockGetter, blockPos, object) -> true));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, false));
    }

    @Override
    protected @NotNull MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = super.getStateForPlacement(blockPlaceContext);
        if (blockState == null) {
            blockState = this.defaultBlockState();
        }
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        boolean isWater = fluidState.is(Fluids.WATER);
        return blockState.setValue(WATERLOGGED, isWater);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (state.getValue(FACE)) {
            case FLOOR -> FLOOR_AABB;
            case WALL -> switch (direction) {
                case EAST -> EAST_AABB;
                case WEST -> WEST_AABB;
                case SOUTH -> SOUTH_AABB;
                case NORTH, UP, DOWN -> NORTH_AABB;
            };
            default -> CEILING_AABB;
        };
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, WATERLOGGED);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
