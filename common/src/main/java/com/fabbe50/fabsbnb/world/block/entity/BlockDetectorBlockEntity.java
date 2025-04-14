package com.fabbe50.fabsbnb.world.block.entity;

import com.fabbe50.fabsbnb.Utilities;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockDetectorBlockEntity extends BlockEntity {
    private CompoundTag stateTag;
    private boolean isFinalized = false;
    private BlockState stateToCheckFor;

    public BlockDetectorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public BlockDetectorBlockEntity(BlockPos pos, BlockState state) {
        this(ModRegistries.BLOCK_DETECTOR_BLOCK_ENTITY.get(), pos, state);
    }

    public void setStateToCheckFor(BlockState stateToCheckFor) {
        this.stateToCheckFor = stateToCheckFor;
    }

    public BlockState getStateToCheckFor() {
        if (this.level != null && this.stateTag != null && !this.isFinalized) {
            this.stateToCheckFor = NbtUtils.readBlockState(Utilities.getBlockRegistryLookup(this.level.registryAccess()), this.stateTag);
            this.stateTag = null;
            this.isFinalized = true;
        }
        return this.stateToCheckFor;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("stateToCheckFor")) {
            this.stateTag = compoundTag.getCompound("stateToCheckFor");
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

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        if (this.stateToCheckFor != null) {
            compoundTag.put("stateToCheckFor", NbtUtils.writeBlockState(this.stateToCheckFor));
        }
        return compoundTag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
