package com.fabbe50.fabsbnb.world.block;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.world.block.base.ExtBaseEntityBlock;
import com.fabbe50.fabsbnb.world.block.entity.XPHolderBlockEntity;
import com.fabbe50.fabsbnb.world.block.interfaces.ILeftClickable;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class XPHolderBlock extends ExtBaseEntityBlock implements ILeftClickable {
    public static final MapCodec<XPHolderBlock> CODEC = simpleCodec(XPHolderBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 12.0F, 16.0F);
    private int cooldown = 20;

    public XPHolderBlock(Properties properties) {
        super(1, properties.sound(SoundType.ANVIL).strength(2.5f));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            FabsBnB.log("Is client side...");
            return InteractionResult.SUCCESS;
        } else {
            FabsBnB.log("Trying to use with item...");
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof XPHolderBlockEntity xpHolder) {
                if (player.getItemInHand(interactionHand).is(Items.REDSTONE_TORCH)) {
                    boolean collectXP = xpHolder.toggleCollectXP();
                    ((ServerPlayer) player).sendSystemMessage(LangUtils.conditionWithStyle(LangUtils.getTextKey("xp_holder.collect"), collectXP), true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            FabsBnB.log("Is client side...");
            return InteractionResult.SUCCESS;
        } else {
            FabsBnB.log("Trying to add XP...");
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            FabsBnB.log("BE: " + blockEntity);
            if (blockEntity instanceof XPHolderBlockEntity xpHolder) {
                FabsBnB.log("BE is XP Holder");
                if (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
                    FabsBnB.log("Player hands are empty.");
                    if (player.isShiftKeyDown()) {
                        xpHolder.storeXP(player, -1);
                    } else {
                        xpHolder.storeXP(player, 1);
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onLeftClick(Level level, BlockPos pos, Player player, InteractionHand hand, Direction side) {
        if (cooldown < 1) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof XPHolderBlockEntity xpHolder) {
                if (player.isShiftKeyDown()) {
                    xpHolder.takeXP(player, 10);
                } else {
                    xpHolder.takeXP(player, 1);
                }
                cooldown = 6;
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, stack);
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof XPHolderBlockEntity xpHolder) {
                CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                if (compoundTag.contains("xp")) {
                    xpHolder.setXp(compoundTag.getInt("xp").orElse(0));
                }
            }
        }
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof XPHolderBlockEntity xpHolder) {
            if (!level.isClientSide && player.isCreative() && xpHolder.getXp() != 0) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putInt("xp", xpHolder.getXp());
                ItemStack stack = new ItemStack(ModRegistries.ITEM_XP_HOLDER.get());
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
                ItemEntity itemEntity = new ItemEntity(level, blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, stack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
                return blockState;
            }
        }
        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(blockState, builder);
        BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof XPHolderBlockEntity xpHolder && xpHolder.getXp() != 0) {
            Level level = xpHolder.getLevel();
            ItemStack stack = new ItemStack(this);
            if (level != null) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putInt("xp", xpHolder.getXp());
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
                drops.clear();
                drops.add(stack);
            }
        }
        return drops;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
        CustomData custom = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (!custom.isEmpty()) {
            CompoundTag tag = custom.copyTag();
            if (tag.contains("xp")) {
                consumer.accept(LangUtils.withValue(LangUtils.getTextKey("xp_holder.stored_level"), String.valueOf(Utilities.getLevelFromTotalExperience(tag.getInt("xp").orElse(-1)))));
            }
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return (level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof XPHolderBlockEntity xpHolder) {
                    xpHolder.tickClient();
                }
            };
        }
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof XPHolderBlockEntity xpHolder) {
                xpHolder.tickServer();
            }
            tick(blockState1, (ServerLevel) level1, blockPos, level1.getRandom());
        };
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new XPHolderBlockEntity(blockPos, blockState);
    }
}
