package com.fabbe50.fabsbnb.forge;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public class PlatformImpl {
    public static TagKey<Block> getOresTag() {
        return Tags.Blocks.ORES;
    }
}
