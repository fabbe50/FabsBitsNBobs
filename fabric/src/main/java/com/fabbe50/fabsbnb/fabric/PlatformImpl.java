package com.fabbe50.fabsbnb.fabric;

import com.fabbe50.fabsbnb.fabric.integration.Trinkets;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class PlatformImpl {
    public static TagKey<Block> getOresTag() {
        return ConventionalBlockTags.ORES;
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isWearingNecklace(LivingEntity entity) {
        if (isModLoaded("trinkets") && !FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return Trinkets.isWearingTrinket(entity, ModRegistries.CHOCOLATE_NECKLACE.get());
        }
        return false;
    }

    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ingredient), output);
        builder.registerPotionRecipe(Potions.WATER, Ingredient.of(ingredient), Potions.MUNDANE);
    }

    public static void registerPotion(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        builder.registerPotionRecipe(input, Ingredient.of(ingredient), output);
    }
}
