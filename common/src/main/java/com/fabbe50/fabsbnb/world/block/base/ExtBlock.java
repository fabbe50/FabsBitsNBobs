package com.fabbe50.fabsbnb.world.block.base;

import com.fabbe50.fabsbnb.world.block.interfaces.IDropSelf;
import net.minecraft.world.level.block.Block;

public abstract class ExtBlock extends Block implements IDropSelf {
    public ExtBlock(Properties properties) {
        super(properties);
    }
}
