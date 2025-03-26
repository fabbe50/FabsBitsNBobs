package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.world.block.base.ExtBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class CustomSpongeBlock extends ExtBlock {
    private static final Direction[] ALL_DIRECTIONS = Direction.values();
    private static TagKey<Fluid> FLUID_TO_CLEAR;

    public CustomSpongeBlock(Properties properties) {
        super(properties.strength(0.6f).sound(SoundType.GRASS));
        FLUID_TO_CLEAR = null;
    }

    public CustomSpongeBlock(TagKey<Fluid> fluidToClear, Properties properties) {
        super(properties.strength(0.6f).sound(SoundType.GRASS));
        FLUID_TO_CLEAR = fluidToClear;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState2.is(blockState.getBlock())) {
            this.tryToAbsorbLiquid(level, blockPos);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        this.tryToAbsorbLiquid(level, blockPos);
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
    }

    protected void tryToAbsorbLiquid(Level level, BlockPos blockPos) {
        if (this.searchAndRemoveLiquid(level, blockPos)) {
            level.setBlock(blockPos, ModRegistries.LAVA_SPONGE_USED.get().defaultBlockState(), 2);
            level.playSound(null, blockPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private boolean searchAndRemoveLiquid(Level level, BlockPos blockPos) {
        return BlockPos.breadthFirstTraversal(blockPos, 15, 513, (blockPosX, consumer) -> {
            for (Direction direction : ALL_DIRECTIONS) {
                consumer.accept(blockPosX.relative(direction));
            }

        }, (blockPos2) -> {
            if (blockPos2.equals(blockPos)) {
                return true;
            } else {
                BlockState blockState = level.getBlockState(blockPos2);
                FluidState fluidState = level.getFluidState(blockPos2);
                if (fluidState.is(FLUID_TO_CLEAR)) {
                    Block block = blockState.getBlock();
                    if (block instanceof BucketPickup bucketPickup) {
                        if (!bucketPickup.pickupBlock(level, blockPos2, blockState).isEmpty()) {
                            return true;
                        }
                    }

                    if (blockState.getBlock() instanceof LiquidBlock) {
                        level.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 3);
                    } else {
                        return false;
                    }

                    return true;
                }
                return false;
            }
        }) > 1;
    }
}
