package com.fabbe50.fabsbnb.neoforge.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.crafting.TransmuteResult;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SmithingRecipeBuilder {
    private final Optional<Ingredient> template;
    private final Ingredient base;
    private final Ingredient addition;
    private final RecipeCategory category;
    private final Item result;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SmithingRecipeBuilder(Optional<Ingredient> arg, Ingredient arg2, Ingredient arg3, RecipeCategory arg4, Item arg5) {
        this.category = arg4;
        this.template = arg;
        this.base = arg2;
        this.addition = arg3;
        this.result = arg5;
    }

    public static SmithingRecipeBuilder smithing(@Nullable Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result) {
        if (template == null) {
            return new SmithingRecipeBuilder(Optional.empty(), base, addition, category, result);
        } else {
            return new SmithingRecipeBuilder(Optional.of(template), base, addition, category, result);
        }
    }

    public SmithingRecipeBuilder unlocks(String string, Criterion<?> arg) {
        this.criteria.put(string, arg);
        return this;
    }

    public void save(RecipeOutput arg, String string) {
        this.save(arg, ResourceKey.create(Registries.RECIPE, ResourceLocation.parse(string)));
    }

    public void save(RecipeOutput arg, ResourceKey<Recipe<?>> arg2) {
        this.ensureValid(arg2);
        Advancement.Builder advancement$builder = arg.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(arg2)).rewards(AdvancementRewards.Builder.recipe(arg2)).requirements(AdvancementRequirements.Strategy.OR);
        Objects.requireNonNull(advancement$builder);
        this.criteria.forEach(advancement$builder::addCriterion);
        SmithingTransformRecipe smithingtransformrecipe = new SmithingTransformRecipe(this.template, this.base, Optional.of(this.addition), new TransmuteResult(this.result));
        arg.accept(arg2, smithingtransformrecipe, advancement$builder.build(arg2.location().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceKey<Recipe<?>> arg) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + arg.location());
        }
    }
}
