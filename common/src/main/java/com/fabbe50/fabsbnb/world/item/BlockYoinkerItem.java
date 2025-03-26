package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.data.YoinkerData;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.world.item.base.ModItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockYoinkerItem extends ModItem {
    public BlockYoinkerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        BlockPos pos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        Player player = useOnContext.getPlayer();
        ItemStack yoinker = useOnContext.getItemInHand();
        BlockState savedState = YoinkerData.getBlockState(level.registryAccess(), yoinker);

        if (savedState.getBlock() == Blocks.AIR) {
            if (blockState.is(ModRegistries.BLOCK_YOINKER_BLACKLIST)) {
                return InteractionResult.FAIL;
            }
            if (blockState.hasBlockEntity()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (player != null && player.isShiftKeyDown()) {
                    if (blockEntity != null) {
                        YoinkerData.setBlockEntity(yoinker, blockEntity);
                        if (blockEntity instanceof Container container) {
                            ContainerHelper.clearOrCountMatchingItems(container, stack -> true, -1, false);
                            if (blockEntity instanceof FurnaceBlockEntity furnace) {
                                furnace.setRecipeUsed(null);
                            }
                        }
                        return this.yoinkBlock(yoinker, blockState, (ServerLevel) level, pos);
                    }
                }
                return InteractionResult.FAIL;
            } else {
                return this.yoinkBlock(yoinker, blockState, (ServerLevel) level, pos);
            }
        } else {
            Direction face = useOnContext.getClickedFace();
            if (blockState.hasBlockEntity()) {
                if (player != null && player.isShiftKeyDown()) {
                    if (this.placeBlock((ServerLevel) level, pos.relative(face), savedState).equals(InteractionResult.FAIL)) {
                        return InteractionResult.FAIL;
                    }
                }
            } else {
                if (this.placeBlock((ServerLevel) level, pos.relative(face), savedState).equals(InteractionResult.FAIL)) {
                    return InteractionResult.FAIL;
                }
            }
            if (YoinkerData.hasBlockEntityData(yoinker)) {
                level.setBlockEntity(BlockEntity.loadStatic(pos.relative(face), savedState, YoinkerData.getBlockEntityData(yoinker)));
            }
            YoinkerData.clearData(yoinker);
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult yoinkBlock(ItemStack yoinker, BlockState state, ServerLevel level, BlockPos pos) {
        YoinkerData.setBlockState(yoinker, state);
        if (!level.destroyBlock(pos, false)) {
            return InteractionResult.FAIL;
        }
        doParticlesAndSound(level, pos);
        return InteractionResult.SUCCESS;
    }

    private InteractionResult placeBlock(ServerLevel level, BlockPos pos, BlockState state) {
        if (!level.setBlockAndUpdate(pos, state)) {
            return InteractionResult.FAIL;
        }
        doParticlesAndSound(level, pos);
        return InteractionResult.SUCCESS;
    }

    private void doParticlesAndSound(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 100; i++) {
            double posX = pos.getX() + level.getRandom().nextDouble();
            double posY = pos.getY() + level.getRandom().nextDouble();
            double posZ = pos.getZ() + level.getRandom().nextDouble();
            double velX = posX >= pos.getX() + 0.5 ? -getRandomVelocity(level) : getRandomVelocity(level);
            double velY = posY >= pos.getY() + 0.5 ? -getRandomVelocity(level) : getRandomVelocity(level);
            double velZ = posZ >= pos.getZ() + 0.5 ? -getRandomVelocity(level) : getRandomVelocity(level);
            level.sendParticles(ParticleTypes.REVERSE_PORTAL, posX, posY, posZ, 1, velX, velY, velZ, 0);
        }
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1, Mth.clamp(level.getRandom().nextFloat(), 0.5f, 1));
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return YoinkerData.hasData(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (level != null) {
            BlockState blockState = YoinkerData.getBlockState(level.registryAccess(), itemStack);
            if (blockState.getBlock() != Blocks.AIR) {
                list.add(Component.translatable("item.fabsbnb.block_yoinker.contains", blockState.getBlock().arch$registryName()).withStyle(ChatFormatting.GRAY));
            } else {
                list.add(Component.translatable("item.fabsbnb.block_yoinker.contains", Component.translatable("item.fabsbnb.block_yoinker.empty")).withStyle(ChatFormatting.GRAY));
            }
            list.add(Component.empty());
            list.add(Component.translatable("item.fabsbnb.block_yoinker.desc").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    private double getRandomVelocity(Level level) {
        return (level.getRandom().nextInt(0, 50) / 100D) * 0.02;
    }
}
