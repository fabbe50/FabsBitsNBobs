package com.fabbe50.fabsbnb.world.block.interfaces;

import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ILeftClickable {
    EventResult onLeftClick(Level level, BlockPos pos, Player player, InteractionHand hand, Direction side);
}
