package com.fabbe50.fabsbnb.forge.datagen.registrysets.sets;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing.Builder;
import net.minecraft.world.item.alchemy.Potions;

public class PotionBrewing {
    public static net.minecraft.world.item.alchemy.PotionBrewing bootstrap(FeatureFlagSet featureFlagSet) {
        Builder builder = new Builder(featureFlagSet);
        addMixes(builder);
        return builder.build();
    }

    public static void addMixes(Builder builder) {
        builder.addMix(Potions.AWKWARD, ModRegistries.CAT_CLAW.get(), ModRegistries.FELINE_AURA_POTION_SHORT);
        builder.addMix(ModRegistries.FELINE_AURA_POTION_SHORT, Items.REDSTONE, ModRegistries.FELINE_AURA_POTION_LONG);
    }
}
