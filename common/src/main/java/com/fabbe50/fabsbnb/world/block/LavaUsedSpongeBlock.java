package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.world.block.interfaces.ITooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class LavaUsedSpongeBlock extends CustomUsedSpongeBlock {
    private final Direction[] FACES_TO_CHECK = new Direction[] { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public LavaUsedSpongeBlock(Properties properties) {
        super(1, ParticleTypes.DRIPPING_LAVA, properties);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level.getBiome(blockPos).value().coldEnoughToSnow(blockPos, level.getSeaLevel())) {
            if (blockPos.getY() >= level.getMinY() && blockPos.getY() < level.getMaxY() && level.getBrightness(LightLayer.BLOCK, blockPos) < 10) {
                replaceWithNonUsed(level, blockPos);
            }
        } else if (level.isWaterAt(blockPos.west()) || level.isWaterAt(blockPos.north()) || level.isWaterAt(blockPos.east()) || level.isWaterAt(blockPos.south()) || level.isWaterAt(blockPos.above())) {
            replaceWithNonUsed(level, blockPos);
        }
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean bl) {
        for (Direction face : FACES_TO_CHECK) {
            BlockPos pos = blockPos.relative(face);
            if (level.isWaterAt(pos)) {
                replaceWithNonUsed(level, blockPos);
            }
        }
    }

    private void replaceWithNonUsed(Level level, BlockPos blockPos) {
        level.setBlock(blockPos, ModRegistries.LAVA_SPONGE.get().defaultBlockState(), 3);
        level.levelEvent(2009, blockPos, 0);
        level.playSound(null, blockPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
    }
}
