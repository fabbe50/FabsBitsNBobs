package com.fabbe50.fabsbnb.fabric;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class PlatformImpl {
    public static TagKey<Block> getOresTag() {
        return ConventionalBlockTags.ORES;
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
