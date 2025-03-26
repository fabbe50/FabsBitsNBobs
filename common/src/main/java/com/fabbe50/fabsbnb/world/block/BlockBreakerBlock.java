package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.world.block.entity.BlockBreakerBlockEntity;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BlockBreakerBlock extends AbstractDispenserLikeBlock {
    public BlockBreakerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void doThings(BlockState state, ServerLevel level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos posInFront = pos.relative(facing);
        BlockState stateInFront = level.getBlockState(posInFront);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockBreakerBlockEntity breakerBlockEntity) {
            ItemStack toolStack = breakerBlockEntity.getItem(0);
            if ((!toolStack.isEmpty() || !stateInFront.requiresCorrectToolForDrops())) {
                if (!stateInFront.requiresCorrectToolForDrops()) {
                    breakBlock(level, posInFront, stateInFront, ItemStack.EMPTY);
                    if (toolStack.getItem() instanceof DiggerItem) {
                        toolStack.setDamageValue(toolStack.getDamageValue() + 1);
                    }
                } else if (toolStack.getItem() instanceof DiggerItem diggerItem && diggerItem.isCorrectToolForDrops(stateInFront)) {
                    breakBlock(level, posInFront, stateInFront, toolStack);
                    toolStack.setDamageValue(toolStack.getDamageValue() + 1);
                } else {
                    level.levelEvent(1001, pos, 0);
                    level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(breakerBlockEntity.getBlockState()));
                }
            } else {
                level.levelEvent(1001, pos, 0);
                level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(breakerBlockEntity.getBlockState()));
            }
        }
    }

    private void breakBlock(ServerLevel level, BlockPos pos, BlockState blockToBreak, ItemStack tool) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        LootParams.Builder builder = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
        blockToBreak.getDrops(builder).forEach((stackX) -> popResource(level, pos, stackX));
        blockToBreak.spawnAfterBreak(level, pos, tool, true);
        if (level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())) {
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(null, blockToBreak));
            level.playSound(null, pos, blockToBreak.getBlock().getSoundType(blockToBreak).getBreakSound(), SoundSource.BLOCKS);
        }
    }

    @Override
    protected void use(Player player, BlockEntity blockEntity) {
        if (blockEntity instanceof BlockBreakerBlockEntity breakerBlockEntity) {
            if (player instanceof ServerPlayer serverPlayer) {
                MenuRegistry.openMenu(serverPlayer, breakerBlockEntity);
            }
        }
    }

    @Override
    protected void setPlacedBy(BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity instanceof BlockBreakerBlockEntity breakerBlockEntity) {
            breakerBlockEntity.setCustomName(stack.getHoverName());
        }
    }

    @Override
    protected void onRemove(Level level, BlockPos pos, BlockEntity blockEntity) {
        if (blockEntity instanceof BlockBreakerBlockEntity breakerBlockEntity) {
            Containers.dropContents(level, pos, breakerBlockEntity);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockBreakerBlockEntity(blockPos, blockState);
    }
}
