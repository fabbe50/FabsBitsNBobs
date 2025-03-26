package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.world.block.base.ExtBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class CustomUsedSpongeBlock extends ExtBlock {
    public static SimpleParticleType particleType;

    public CustomUsedSpongeBlock(Properties properties) {
        super(properties.strength(0.6f).sound(SoundType.GRASS));
        particleType = null;
    }

    public CustomUsedSpongeBlock(SimpleParticleType particle, Properties properties) {
        super(properties.strength(0.6f).sound(SoundType.GRASS));
        particleType = particle;
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (particleType != null) {
            Direction direction = Direction.getRandom(randomSource);
            if (direction != Direction.UP) {
                BlockPos blockPos2 = blockPos.relative(direction);
                BlockState blockState2 = level.getBlockState(blockPos2);
                if (!blockState.canOcclude() || !blockState2.isFaceSturdy(level, blockPos2, direction.getOpposite())) {
                    double d = blockPos.getX();
                    double e = blockPos.getY();
                    double f = blockPos.getZ();
                    if (direction == Direction.DOWN) {
                        e -= 0.05;
                        d += randomSource.nextDouble();
                        f += randomSource.nextDouble();
                    } else {
                        e += randomSource.nextDouble() * 0.8;
                        if (direction.getAxis() == Direction.Axis.X) {
                            f += randomSource.nextDouble();
                            if (direction == Direction.EAST) {
                                ++d;
                            } else {
                                d += 0.05;
                            }
                        } else {
                            d += randomSource.nextDouble();
                            if (direction == Direction.SOUTH) {
                                ++f;
                            } else {
                                f += 0.05;
                            }
                        }
                    }

                    level.addParticle(particleType, d, e, f, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}
