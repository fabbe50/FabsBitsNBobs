package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.world.item.base.ModItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WrenchItem extends ModItem {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        Player player = useOnContext.getPlayer();
        Direction face = useOnContext.getClickedFace();
        BlockPos pos = useOnContext.getClickedPos();
        BlockState state = useOnContext.getLevel().getBlockState(pos);

        if (state.hasProperty(DirectionalBlock.FACING)) {
            Direction facing = state.getValue(DirectionalBlock.FACING);
            if (player != null) {
                switch (player.getDirection()) {
                    case NORTH, WEST, UP, DOWN -> facing = facing.getClockWise(face.getAxis());
                    case SOUTH, EAST -> facing = facing.getCounterClockWise(face.getAxis());
                }
            } else {
                facing = facing.getClockWise(face.getAxis());
            }
            level.setBlockAndUpdate(pos, state.setValue(DirectionalBlock.FACING, facing));
            return InteractionResult.SUCCESS;
        }
        if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            if (face.getAxis().isVertical()) {
                Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
                facing = facing.getClockWise(face.getAxis());
                level.setBlockAndUpdate(pos, state.setValue(HorizontalDirectionalBlock.FACING, facing));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        list.add(Component.translatable("item.fabsbnb.wrench.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
