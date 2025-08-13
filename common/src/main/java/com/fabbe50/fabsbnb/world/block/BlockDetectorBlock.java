package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.world.block.base.ExtBaseEntityBlock;
import com.fabbe50.fabsbnb.world.block.entity.BlockDetectorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockDetectorBlock extends ExtBaseEntityBlock {
    public static final MapCodec<BlockDetectorBlock> CODEC = simpleCodec(BlockDetectorBlock::new);
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockDetectorBlock(Properties properties) {
        super(properties.mapColor(MapColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops().strength(3.5f));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.SOUTH).setValue(POWERED, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack heldItem = player.getItemInHand(player.getUsedItemHand());
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BlockDetectorBlockEntity detectorBlockEntity && player instanceof ServerPlayer serverPlayer) {
                if (heldItem.is(Items.REDSTONE_TORCH)) {
                    Direction facing = blockState.getValue(FACING);
                    BlockState blockInFront = level.getBlockState(blockPos.relative(facing));
                    if (detectorBlockEntity.getStateToCheckFor() == null || !detectorBlockEntity.getStateToCheckFor().equals(blockInFront)) {
                        detectorBlockEntity.setStateToCheckFor(blockInFront);
                        ResourceLocation location = blockInFront.getBlock().arch$registryName();
                        if (location != null) {
                            serverPlayer.sendSystemMessage(Component.translatable(FabsBnB.translation("detector.set"), Component.literal(location.toString())), true);
                        }
                        this.checkConditionsAndUpdate(blockState, level, blockPos);
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    BlockState stateToCheckFor = detectorBlockEntity.getStateToCheckFor();
                    if (stateToCheckFor == null) {
                        serverPlayer.sendSystemMessage(Component.translatable(FabsBnB.translation("detector.info"), Component.translatable("item.fabsbnb.block_yoinker.empty")), true);
                    } else {
                        ResourceLocation location = stateToCheckFor.getBlock().arch$registryName();
                        if (location != null) {
                            serverPlayer.sendSystemMessage(Component.translatable(FabsBnB.translation("detector.info"), Component.literal(location.toString())), true);
                        }
                    }
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        this.checkConditionsAndUpdate(blockState, serverLevel, blockPos);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(FACING) == direction && !blockState.getValue(POWERED)) {
            if (!levelAccessor.isClientSide() && !levelAccessor.getBlockTicks().hasScheduledTick(blockPos, this)) {
                levelAccessor.scheduleTick(blockPos, this, 2);
            }
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        this.checkConditionsAndUpdate(blockState, level, blockPos, blockPos2);
    }

    private void checkConditionsAndUpdate(BlockState blockState, Level level, BlockPos blockPos) {
        this.checkConditionsAndUpdate(blockState, level, blockPos, blockPos.relative(blockState.getValue(FACING)));
    }

    private void checkConditionsAndUpdate(BlockState blockState, Level level, BlockPos blockPos, BlockPos blockPos2) {
        Direction facing = blockState.getValue(FACING);
        if (blockPos.relative(facing).equals(blockPos2)) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BlockDetectorBlockEntity detectorBlockEntity) {
                if (detectorBlockEntity.getStateToCheckFor() == null) {
                    return;
                }
                BlockState stateAtPos = level.getBlockState(blockPos2);
                if (detectorBlockEntity.getStateToCheckFor().getBlock().equals(stateAtPos.getBlock()) && !blockState.getValue(POWERED)) {
                    level.setBlock(blockPos, blockState.setValue(POWERED, true), 2);
                    this.updateNeighborsInFront(level, blockPos, blockState);
                } else if (blockState.getValue(POWERED)) {
                    level.setBlock(blockPos, blockState.setValue(POWERED, false), 2);
                    this.updateNeighborsInFront(level, blockPos, blockState);
                }
            }
        }
    }

    protected void updateNeighborsInFront(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(FACING);
        BlockPos blockPos2 = blockPos.relative(direction.getOpposite());
        level.neighborChanged(blockPos2, this, blockPos);
        level.updateNeighborsAtExceptFromFacing(blockPos2, this, direction);
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getSignal(blockGetter, blockPos, direction);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(POWERED) && blockState.getValue(FACING) == direction ? 15 : 0;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock())) {
            if (!level.isClientSide() && !level.getBlockTicks().hasScheduledTick(blockPos, this)) {
                this.checkConditionsAndUpdate(blockState, level, blockPos);
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        this.updateNeighborsInFront(level, blockPos, blockState.setValue(POWERED, false));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getNearestLookingDirection());
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockDetectorBlockEntity(blockPos, blockState);
    }
}
