package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class Recipes extends RecipeProvider {
    public Recipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, completableFuture);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        smithingNoTemplate(ModRegistries.ITEM_LAVA_SPONGE.get(), Items.SPONGE, Items.LAVA_BUCKET)
                .unlocks("has_sponge", has(Items.SPONGE))
                .save(recipeOutput, FabsBnB.location(ModRegistries.ITEM_LAVA_SPONGE.getId().getPath() + "-smithing_upgrade"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModRegistries.ITEM_LAVA_SPONGE.get(), 8)
                .requires(Items.WATER_BUCKET)
                .requires(ModRegistries.ITEM_LAVA_SPONGE_USED.get(), 8)
                .group(FabsBnB.MOD_ID)
                .unlockedBy("has_lava_sponge_used", has(ModRegistries.ITEM_LAVA_SPONGE_USED.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistries.ITEM_BLOCK_YOINKER.get(), 1)
                .pattern("SES")
                .pattern("IRI")
                .pattern(" R ")
                .define('S', Items.SHULKER_SHELL)
                .define('E', Items.ENDER_EYE)
                .define('I', Items.IRON_INGOT)
                .define('R', Items.STICK)
                .group(FabsBnB.MOD_ID + "_yoinker")
                .unlockedBy("has_shulker_shell", has(Items.SHULKER_SHELL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistries.WOODEN_BUILDING_WAND.get(), 1)
                .pattern("P  ")
                .pattern(" S ")
                .pattern("  S")
                .define('P', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .group(FabsBnB.MOD_ID + "_building_wands")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistries.STONE_BUILDING_WAND.get(), 1)
                .pattern("P  ")
                .pattern(" S ")
                .pattern("  S")
                .define('P', ItemTags.STONE_TOOL_MATERIALS)
                .define('S', Items.STICK)
                .unlockedBy("has_planks", has(ItemTags.STONE_TOOL_MATERIALS))
                .group(FabsBnB.MOD_ID)
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistries.IRON_BUILDING_WAND.get(), 1)
                .pattern("P  ")
                .pattern(" S ")
                .pattern("  S")
                .define('P', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_planks", has(Items.IRON_INGOT))
                .group(FabsBnB.MOD_ID + "_building_wands")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistries.GOLD_BUILDING_WAND.get(), 1)
                .pattern("P  ")
                .pattern(" S ")
                .pattern("  S")
                .define('P', Items.GOLD_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_planks", has(Items.GOLD_INGOT))
                .group(FabsBnB.MOD_ID + "_building_wands")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistries.DIAMOND_BUILDING_WAND.get(), 1)
                .pattern("P  ")
                .pattern(" S ")
                .pattern("  S")
                .define('P', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_planks", has(Items.DIAMOND))
                .group(FabsBnB.MOD_ID + "_building_wands")
                .save(recipeOutput);
        smithingWithTemplate(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, ModRegistries.NETHERITE_BUILDING_WAND.get(), ModRegistries.DIAMOND_BUILDING_WAND.get(), Items.NETHERITE_INGOT)
                .unlocks("has_netherite", has(Items.NETHERITE_INGOT))
                .save(recipeOutput, FabsBnB.location(ModRegistries.NETHERITE_BUILDING_WAND.getId().getPath() + "-smithing_upgrade"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModRegistries.ITEM_PUSHER_BLOCK.get(), 8)
                .pattern("ISI")
                .pattern("RSR")
                .pattern("ISI")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.SLIME_BALL)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_slime", has(Items.SLIME_BALL))
                .group(FabsBnB.MOD_ID + "_pushers")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistries.ITEM_THIN_LIGHT.get(), 4)
                .pattern("IRI")
                .pattern("RGR")
                .pattern("IRI")
                .define('I', Items.IRON_NUGGET)
                .define('R', Items.REDSTONE)
                .define('G', Items.GLOWSTONE_DUST)
                .unlockedBy("has_glowstone_dust", has(Items.GLOWSTONE_DUST))
                .group(FabsBnB.MOD_ID + "thin_light")
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModRegistries.ITEM_POWERED_THIN_LIGHT.get(), 1)
                .requires(Items.REDSTONE_TORCH)
                .requires(ModRegistries.ITEM_THIN_LIGHT.get())
                .unlockedBy("has_thin_light", has(ModRegistries.ITEM_THIN_LIGHT.get()))
                .group(FabsBnB.MOD_ID + "powered_thin_light")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistries.WHOOSH_WAND.get(), 1)
                .pattern("  F")
                .pattern(" C ")
                .pattern("S  ")
                .define('F', Items.FIREWORK_ROCKET)
                .define('C', Items.COAL_BLOCK)
                .define('S', Items.STICK)
                .unlockedBy("has_firework_rocket", has(Items.FIREWORK_ROCKET))
                .group(FabsBnB.MOD_ID + "whoosh_wand")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistries.ITEM_BLOCK_PLACER.get(), 1)
                .pattern("CCC")
                .pattern("CWC")
                .pattern("CRC")
                .define('C', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('W', ModRegistries.DIAMOND_BUILDING_WAND.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_building_wand", has(ModRegistries.BUILDING_WANDS))
                .group(FabsBnB.MOD_ID + "placer")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistries.ITEM_BLOCK_BREAKER.get(), 1)
                .pattern("CCC")
                .pattern("CWC")
                .pattern("CRC")
                .define('C', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('W', Items.DIAMOND_PICKAXE)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_diamond_pickaxe", has(Items.DIAMOND_PICKAXE))
                .group(FabsBnB.MOD_ID + "breaker")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistries.ITEM_BLOCK_DETECTOR.get(), 1)
                .pattern("CCC")
                .pattern("CWC")
                .pattern("CRC")
                .define('C', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('W', Items.QUARTZ_BLOCK)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_quartz_block", has(Items.QUARTZ_BLOCK))
                .group(FabsBnB.MOD_ID + "detector")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModRegistries.ITEM_XP_HOLDER.get(), 1)
                .pattern(" E ")
                .pattern("DOD")
                .pattern("OOO")
                .define('E', Items.EMERALD)
                .define('D', Items.DIAMOND)
                .define('O', Items.OBSIDIAN)
                .unlockedBy("has_emerald", has(Items.EMERALD))
                .group(FabsBnB.MOD_ID + "xp_holder")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistries.WRENCH.get(), 1)
                .pattern("I I")
                .pattern(" I ")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .group(FabsBnB.MOD_ID + "wrench")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModRegistries.MILK_BOTTLE.get(), 8)
                .pattern("BBB")
                .pattern("BMB")
                .pattern("BBB")
                .define('B', Items.GLASS_BOTTLE)
                .define('M', Items.MILK_BUCKET)
                .unlockedBy("has_milk", has(Items.MILK_BUCKET))
                .group(FabsBnB.MOD_ID + "milk_bottle")
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModRegistries.CHOCOLATE_MILK_BOTTLE.get(), 1)
                .requires(ModRegistries.MILK_BOTTLE.get())
                .requires(Items.COCOA_BEANS)
                .unlockedBy("has_milk_bottle", has(ModRegistries.MILK_BOTTLE.get()))
                .group(FabsBnB.MOD_ID + "chocolate_milk_bottle")
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModRegistries.CHOCOLATE_NECKLACE.get(), 1)
                .pattern("NGN")
                .pattern("GCG")
                .pattern("NSN")
                .define('N', Items.GOLD_NUGGET)
                .define('G', Items.GOLD_INGOT)
                .define('C', ModRegistries.CHOCOLATE_MILK_BOTTLE.get())
                .define('S', Items.NETHER_STAR)
                .unlockedBy("has_chocolate_milk", has(ModRegistries.CHOCOLATE_MILK_BOTTLE.get()))
                .group(FabsBnB.MOD_ID + "chocolate_necklace")
                .save(recipeOutput);
    }

    private SmithingTransformRecipeBuilder smithingNoTemplate(Item output, Item base, Item addition) {
        return SmithingTransformRecipeBuilder.smithing(Ingredient.EMPTY, Ingredient.of(base), Ingredient.of(addition), RecipeCategory.MISC, output);
    }

    private SmithingTransformRecipeBuilder smithingWithTemplate(Item template, Item output, Item base, Item addition) {
        return SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(addition), RecipeCategory.MISC, output);
    }
}
