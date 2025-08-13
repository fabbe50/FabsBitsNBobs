package com.fabbe50.fabsbnb.world.block.base;

import com.fabbe50.fabsbnb.world.block.interfaces.IDropSelf;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public abstract class ExtHorizontalDirectionalBlock extends HorizontalDirectionalBlock implements IDropSelf {
    public ExtHorizontalDirectionalBlock(Properties properties) {
        super(properties);
    }
}
