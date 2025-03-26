package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.world.block.base.ExtHorizontalDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PusherBlock extends ExtHorizontalDirectionalBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 1.0F, 16.0F);

    public PusherBlock(Properties properties) {
        super(properties.strength(2.0f).sound(SoundType.STONE).isValidSpawn((blockState, blockGetter, blockPos, object) -> true));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!entity.isCrouching()) {
            Direction facing = blockState.getValue(FACING);
            double speedMultiplier = ModConfig.INSTANCE.entityMoverBlockSpeed;
            entity.setDeltaMovement(entity.getDeltaMovement().add(speedMultiplier * (facing.getStepX() * 1.5), 0, speedMultiplier * (facing.getStepZ() * 1.5)));
        }
    }
}
