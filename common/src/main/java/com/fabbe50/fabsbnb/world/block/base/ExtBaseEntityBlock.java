package com.fabbe50.fabsbnb.world.block.base;

import com.fabbe50.fabsbnb.world.block.interfaces.IDropSelf;
import net.minecraft.world.level.block.BaseEntityBlock;

public abstract class ExtBaseEntityBlock extends BaseEntityBlock implements IDropSelf {
    public ExtBaseEntityBlock(Properties properties) {
        super(properties);
    }


}
