package com.fabbe50.fabsbnb;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class Platform {
    @ExpectPlatform
    public static TagKey<Block> getOresTag() {
        throw new AssertionError();
    }
}
