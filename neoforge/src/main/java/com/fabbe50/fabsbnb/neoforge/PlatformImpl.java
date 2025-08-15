package com.fabbe50.fabsbnb.neoforge;

import com.fabbe50.fabsbnb.neoforge.integration.Curios;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;

public class PlatformImpl {
    public static TagKey<Block> getOresTag() {
        return Tags.Blocks.ORES;
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isWearingNecklace(LivingEntity entity) {
        return Curios.isWearingCurio(entity, ModRegistries.CHOCOLATE_NECKLACE.get());
    }
}
