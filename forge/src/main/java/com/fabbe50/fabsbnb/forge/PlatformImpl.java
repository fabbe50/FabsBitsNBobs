package com.fabbe50.fabsbnb.forge;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;

public class PlatformImpl {
    public static TagKey<Block> getOresTag() {
        return Tags.Blocks.ORES;
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
