package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.data.CustomBrewingRecipe;
import dev.architectury.platform.Mod;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PotionBrewingRecipes {
    private static final Set<CustomBrewingRecipe> brewingRecipes = new HashSet<>();
    private static void addBrewingRecipe(CustomBrewingRecipe brewingRecipe) {
        brewingRecipes.add(brewingRecipe);
    }
    public static Set<CustomBrewingRecipe> getBrewingRecipes() {
        return brewingRecipes;
    }

    public static void register(PotionBrewing.Builder builder) {
        FabsBnB.log("Setting up potion brewing...");
        builder.addContainer(ModRegistries.OWN_POTION_ITEM.get());
        builder.addContainer(ModRegistries.OWN_SPLASH_POTION_ITEM.get());
        builder.addContainer(ModRegistries.OWN_LINGERING_POTION_ITEM.get());
        builder.addContainerRecipe(ModRegistries.OWN_POTION_ITEM.get(), Items.GUNPOWDER, ModRegistries.OWN_SPLASH_POTION_ITEM.get());
        builder.addContainerRecipe(ModRegistries.OWN_SPLASH_POTION_ITEM.get(), Items.DRAGON_BREATH, ModRegistries.OWN_LINGERING_POTION_ITEM.get());

        Holder<Potion> felineShort = ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_SHORT);
        Holder<Potion> felineLong = ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_LONG);

        registerStarter(builder, ModRegistries.CAT_CLAW.get(), felineShort);
        registerPotionRecipe(builder, felineShort, Items.REDSTONE, felineLong);

        Holder<Potion> resistanceShort = ModRegistries.getPotionReference(ModRegistries.SCUTE_POTION_SHORT);
        Holder<Potion> resistanceLong = ModRegistries.getPotionReference(ModRegistries.SCUTE_POTION_LONG);

        registerStarter(builder, Items.TURTLE_SCUTE, resistanceShort);
        registerStarter(builder, Items.ARMADILLO_SCUTE, resistanceShort);
        registerPotionRecipe(builder, resistanceShort, Items.REDSTONE, resistanceLong);
    }

    public static ItemStack createItemStack(Item item, Holder<Potion> holder) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(holder));
        return itemStack;
    }

    private static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        addBrewingRecipe(new CustomBrewingRecipe(Items.POTION, Potions.AWKWARD, ingredient, ModRegistries.OWN_POTION_ITEM.get(), output));
        addBrewingRecipe(new CustomBrewingRecipe(Items.SPLASH_POTION, Potions.AWKWARD, ingredient, ModRegistries.OWN_SPLASH_POTION_ITEM.get(), output));
        addBrewingRecipe(new CustomBrewingRecipe(Items.LINGERING_POTION, Potions.AWKWARD, ingredient, ModRegistries.OWN_LINGERING_POTION_ITEM.get(), output));
        Platform.registerStarter(builder, ingredient, output);
    }

    private static void registerPotionRecipe(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        Platform.registerPotion(builder, input, ingredient, output);
    }
}
