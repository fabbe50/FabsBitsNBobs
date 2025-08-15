package com.fabbe50.fabsbnb.fabric;

import com.fabbe50.fabsbnb.fabric.integration.Trinkets;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;

public class PlatformImpl {
    public static TagKey<Block> getOresTag() {
        return ConventionalBlockTags.ORES;
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isWearingNecklace(LivingEntity entity) {
        return Trinkets.isWearingTrinket(entity, ModRegistries.CHOCOLATE_NECKLACE.get());
    }
}
