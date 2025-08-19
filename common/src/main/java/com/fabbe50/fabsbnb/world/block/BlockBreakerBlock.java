package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.registries.EventRegistry;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.block.entity.BlockBreakerBlockEntity;
import com.mojang.serialization.MapCodec;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockBreakerBlock extends AbstractDispenserLikeBlock {
    public static final MapCodec<BlockBreakerBlock> CODEC = simpleCodec(BlockBreakerBlock::new);

    public BlockBreakerBlock(Properties properties) {
        super(properties);
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
        if (blockEntity instanceof BlockBreakerBlockEntity breakerBlockEntity) {
            ItemStack toolStack = breakerBlockEntity.getItem(0);
            boolean toolRequired = stateInFront.requiresCorrectToolForDrops();
            boolean blockBroken = false;
            if (toolStack.isEmpty() && !toolRequired) {
                blockBroken = breakBlock(level, posInFront, stateInFront, ItemStack.EMPTY);
            } else if (!toolStack.isEmpty()) {
                if (toolStack.getItem() instanceof DiggerItem diggerItem) {
                    if (diggerItem.isCorrectToolForDrops(toolStack, stateInFront)) {
                        blockBroken = breakBlock(level, posInFront, stateInFront, toolStack);
                        Utilities.hurtItem(1, level, toolStack, pos);
                    } else if (!toolRequired) {
                        blockBroken = breakBlock(level, posInFront, stateInFront, toolStack);
                        Utilities.hurtItem(2, level, toolStack, pos);
                    }
                }
            }
            if (!blockBroken) {
                level.levelEvent(1001, pos, 0);
                level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(breakerBlockEntity.getBlockState()));
            }
        }
    }

    private boolean breakBlock(ServerLevel level, BlockPos pos, BlockState blockToBreak, ItemStack tool) {
        if (EventRegistry.handleCustomMiningEnchantments(level, pos, blockToBreak, tool)) {
            return true;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        LootParams.Builder builder = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
        blockToBreak.getDrops(builder).forEach((stackX) -> popResource(level, pos, stackX));
        blockToBreak.spawnAfterBreak(level, pos, tool, true);
        if (level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())) {
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(null, blockToBreak));
            level.playSound(null, pos, blockToBreak.getSoundType().getBreakSound(), SoundSource.BLOCKS);
            return true;
        }
        return false;
    }

    @Override
    protected InteractionResult use(Player player, BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockBreakerBlockEntity) {
            if (player instanceof ServerPlayer serverPlayer) {
                MenuRegistry.openExtendedMenu(serverPlayer, this.getMenuProvider(state, level, pos));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
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

    @Override
    protected @Nullable ExtendedMenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        return new ExtendedMenuProvider() {
            @Override
            public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof BlockBreakerBlockEntity blockBreakerBlockEntity) {
                    return blockBreakerBlockEntity.createMenu(i, inventory, player);
                }
                return null;
            }

            @Override
            public @NotNull Component getDisplayName() {
                return LangUtils.getComponent(BlockBreakerBlock.this);
            }

            @Override
            public void saveExtraData(FriendlyByteBuf buf) {
                buf.writeBlockPos(blockPos);
            }
        };
    }
}
