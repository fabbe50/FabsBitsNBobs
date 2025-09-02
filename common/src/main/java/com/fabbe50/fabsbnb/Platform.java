package com.fabbe50.fabsbnb;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.block.Block;

public class Platform {
    @ExpectPlatform
    public static TagKey<Block> getOresTag() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWearingNecklace(LivingEntity entity) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerPotion(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        throw new AssertionError();
    }
}
