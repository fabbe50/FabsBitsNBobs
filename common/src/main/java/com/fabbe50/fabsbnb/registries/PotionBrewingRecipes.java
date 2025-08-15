package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;

public class PotionBrewingRecipes {
    public static void register(PotionBrewing.Builder builder) {
        FabsBnB.log("Setting up potion brewing...");
        Holder<Potion> felineShort = ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_SHORT);
        Holder<Potion> felineLong = ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_LONG);

        builder.addStartMix(ModRegistries.CAT_CLAW.get(), felineShort);
        builder.addMix(felineShort, Items.REDSTONE, felineLong);
    }
}
