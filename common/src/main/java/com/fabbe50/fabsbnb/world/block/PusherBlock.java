package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.world.block.base.ExtHorizontalDirectionalBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PusherBlock extends ExtHorizontalDirectionalBlock {
    public static final MapCodec<PusherBlock> CODEC = simpleCodec(PusherBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 1.0F, 16.0F);

    public PusherBlock(Properties properties) {
        super(1, properties.strength(2.0f).sound(SoundType.STONE).isValidSpawn((blockState, blockGetter, blockPos, object) -> true));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return canAttach(levelReader, blockPos, Direction.DOWN);
    }

    public static boolean canAttach(LevelReader levelReader, BlockPos blockPos, Direction direction) {
        BlockPos pos2 = blockPos.relative(direction);
        return levelReader.getBlockState(pos2).isFaceSturdy(levelReader, blockPos, direction.getOpposite());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
        return SHAPE;
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        return !blockState.canSurvive(levelReader, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier) {
        if (!entity.isCrouching()) {
            Direction facing = blockState.getValue(FACING).getOpposite();
            double speedMultiplier = ModConfig.entityMoverBlockSpeed.getValue();
            if (ModConfig.experimentalSquidPushing.getValue() && entity instanceof Squid squid) {
                squid.move(MoverType.SELF, new Vec3(speedMultiplier * (facing.getStepX() * 1.5), 0, speedMultiplier * (facing.getStepZ() * 1.5)));
            }
            entity.setDeltaMovement(entity.getDeltaMovement().add(speedMultiplier * (facing.getStepX() * 1.5), 0, speedMultiplier * (facing.getStepZ() * 1.5)));
        }
    }
}
