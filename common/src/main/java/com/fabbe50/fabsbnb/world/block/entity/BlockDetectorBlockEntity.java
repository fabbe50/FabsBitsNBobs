package com.fabbe50.fabsbnb.world.block.entity;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockDetectorBlockEntity extends BlockEntity {
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
        return this.stateToCheckFor;
    }

    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        valueInput.read("stateToCheckFor", BlockState.CODEC).ifPresent(state -> this.stateToCheckFor = state);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        if (this.stateToCheckFor != null) {
            valueOutput.store("stateToCheckFor", BlockState.CODEC, this.stateToCheckFor);
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag compoundTag = super.getUpdateTag(provider);
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
