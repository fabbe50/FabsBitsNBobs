package com.fabbe50.fabsbnb.fabric;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class PlatformImpl {
    public static TagKey<Block> getOresTag() {
        return ConventionalBlockTags.ORES;
    }
}
