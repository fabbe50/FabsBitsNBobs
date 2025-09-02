package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.world.block.entity.BlockPlacerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockPlacerBlock extends AbstractDispenserLikeBlock {
    public static final MapCodec<BlockPlacerBlock> CODEC = simpleCodec(BlockPlacerBlock::new);

    public BlockPlacerBlock(Properties properties) {
        super(1, properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void doThings(BlockState state, ServerLevel level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos posInFront = pos.relative(facing);
        BlockState stateInFront = level.getBlockState(posInFront);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockPlacerBlockEntity placerBlockEntity) {
            int slot = placerBlockEntity.getRandomSlot(level.getRandom());
            if (slot != -1 && stateInFront.canBeReplaced()) {
                ItemStack blockToPlace = placerBlockEntity.getItem(slot);
                if (blockToPlace.getItem() instanceof BlockItem blockItem) {
                    blockToPlace.shrink(1);
                    Block block = blockItem.getBlock();
                    BlockState blockState = block.defaultBlockState();
                    if (level.setBlock(posInFront, blockState, 11)) {
                        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(blockState));
                        level.playSound(null, pos, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS);
                    }
                }
            } else {
                level.levelEvent(1001, pos, 0);
                level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(placerBlockEntity.getBlockState()));
            }
        }
    }

    @Override
    protected InteractionResult use(Player player, BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockPlacerBlockEntity placerBlockEntity) {
            player.openMenu(placerBlockEntity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void onRemove(Level level, BlockPos pos, BlockEntity blockEntity) {
        if (blockEntity instanceof BlockPlacerBlockEntity placerBlockEntity) {
            Containers.dropContents(level, pos, placerBlockEntity);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockPlacerBlockEntity(blockPos, blockState);
    }
}
