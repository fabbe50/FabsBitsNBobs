package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.util.LangUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LavaUsedSpongeBlock extends CustomUsedSpongeBlock {
    public LavaUsedSpongeBlock(Properties properties) {
        super(ParticleTypes.DRIPPING_LAVA, properties);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level.getBiome(blockPos).value().coldEnoughToSnow(blockPos)) {
            if (blockPos.getY() >= level.getMinBuildHeight() && blockPos.getY() < level.getMaxBuildHeight() && level.getBrightness(LightLayer.BLOCK, blockPos) < 10) {
                replaceWithNonUsed(level, blockPos);
            }
        } else if (level.isWaterAt(blockPos.west()) || level.isWaterAt(blockPos.north()) || level.isWaterAt(blockPos.east()) || level.isWaterAt(blockPos.south()) || level.isWaterAt(blockPos.above())) {
            replaceWithNonUsed(level, blockPos);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!blockPos.below().equals(blockPos2) && level.isWaterAt(blockPos2)) {
            replaceWithNonUsed(level, blockPos);
        }
    }

    private void replaceWithNonUsed(Level level, BlockPos blockPos) {
        level.setBlock(blockPos, ModRegistries.LAVA_SPONGE.get().defaultBlockState(), 3);
        level.levelEvent(2009, blockPos, 0);
        level.playSound(null, blockPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(LangUtils.getDescription(this));
    }
}
