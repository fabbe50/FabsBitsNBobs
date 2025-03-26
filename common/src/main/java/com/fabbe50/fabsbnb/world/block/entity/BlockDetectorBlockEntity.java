package com.fabbe50.fabsbnb.world.block.entity;

import com.fabbe50.fabsbnb.Utilities;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockDetectorBlockEntity extends BlockEntity {
    private BlockState stateToCheckFor;

    public BlockDetectorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        stateToCheckFor = Blocks.AIR.defaultBlockState();
    }

    public BlockDetectorBlockEntity(BlockPos pos, BlockState state) {
        this(ModRegistries.BLOCK_DETECTOR_BLOCK_ENTITY.get(), pos, state);
    }

    public void setStateToCheckFor(BlockState stateToCheckFor) {
        this.stateToCheckFor = stateToCheckFor;
    }

    public BlockState getStateToCheckFor() {
        return stateToCheckFor;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("stateToCheckFor")) {
            Level level = this.getLevel();
            if (level != null) {
                this.stateToCheckFor = NbtUtils.readBlockState(Utilities.getBlockRegistryLookup(level.registryAccess()), compoundTag.getCompound("stateToCheckFor"));
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (this.stateToCheckFor != null) {
            ResourceLocation location = this.stateToCheckFor.getBlock().arch$registryName();
            if (location != null) {
                CompoundTag blockData = NbtUtils.writeBlockState(this.stateToCheckFor);
                compoundTag.put("stateToCheckFor", blockData);
            }
        }
    }
}
