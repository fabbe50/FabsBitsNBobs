package com.fabbe50.fabsbnb.world.block.base;

import com.fabbe50.fabsbnb.world.block.interfaces.IDropSelf;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;

public abstract class ExtFaceAttachedHorizontalDirectionalBlock extends FaceAttachedHorizontalDirectionalBlock implements IDropSelf {
    public ExtFaceAttachedHorizontalDirectionalBlock(Properties properties) {
        super(properties);
    }
}
