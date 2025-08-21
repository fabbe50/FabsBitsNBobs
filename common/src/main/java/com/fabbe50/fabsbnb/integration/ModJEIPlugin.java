package com.fabbe50.fabsbnb.integration;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.data.CauldronConversionData;
import com.fabbe50.fabsbnb.data.ItemInformations;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.item.enchantments.IEnchantment;
import com.mojang.datafixers.util.Pair;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    public static RecipeType<CauldronRecipe> cauldronType = RecipeType.create(FabsBnB.MOD_ID, "cauldron_conversion", CauldronRecipe.class);
    public static CauldronRecipeCategory cauldronRecipeCategory = new CauldronRecipeCategory(cauldronType, Component.translatable("jei.category.fabsbnb.cauldron_conversion"));

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return FabsBnB.location("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        FabsBnB.log("JEI is loaded! Registering plugin categories...");
        registration.addRecipeCategories(cauldronRecipeCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        FabsBnB.log("JEI is loaded! Registering plugin recipes...");
        List<CauldronRecipe> cauldronRecipes = new ArrayList<>();
        Map<Item, Item> cauldronConversionMap = CauldronConversionData.getConversionMap();
        for (Item input : cauldronConversionMap.keySet()) {
            cauldronRecipes.add(new CauldronRecipe(Ingredient.of(input), Ingredient.of(cauldronConversionMap.get(input))));
        }
        registration.addRecipes(cauldronType, cauldronRecipes);

        if (!Platform.isModLoaded("emi")) {
            Map<String, Pair<List<Item>, List<Component>>> itemComponentsMap = ItemInformations.getItemComponentsMap();
            for (String name : itemComponentsMap.keySet()) {
                for (Item item : itemComponentsMap.get(name).getFirst()) {
                    registration.addIngredientInfo(item, itemComponentsMap.get(name).getSecond().toArray(Component[]::new));
                }
            }
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Map<IEnchantment, List<Component>> enchantmentComponents = ItemInformations.getEnchantmentComponents();
                for (IEnchantment enchantment : enchantmentComponents.keySet()) {
                    for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                        registration.addItemStackInfo(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Utilities.getHolder(level, enchantment.getResourceKey()), i)), enchantmentComponents.get(enchantment).toArray(Component[]::new));
                    }
                }
            }

            Map<String, Pair<List<ItemStack>, List<Component>>> stackComponents = ItemInformations.getItemStackComponents();
            for (String name : stackComponents.keySet()) {
                for (ItemStack stack : stackComponents.get(name).getFirst()) {
                    registration.addItemStackInfo(stack, stackComponents.get(name).getSecond().toArray(Component[]::new));
                }
            }
        }
    }

    public record CauldronRecipe(Ingredient input, Ingredient output) {
    }

    public static class CauldronRecipeCategory extends AbstractRecipeCategory<CauldronRecipe> {
        public CauldronRecipeCategory(RecipeType<CauldronRecipe> recipeType, Component title) {
            super(recipeType, title, new IDrawable() {
                @Override
                public int getWidth() {
                    return 18;
                }

                @Override
                public int getHeight() {
                    return 18;
                }

                @Override
                public void draw(GuiGraphics guiGraphics, int x, int y) {
                    guiGraphics.renderFakeItem(new ItemStack(ModRegistries.FULL_WATER_CAULDRON.get()), x, y);
                }
            }, 108, 18);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, CauldronRecipe cauldronRecipe, IFocusGroup iFocusGroup) {
            IRecipeSlotBuilder inputSlot = builder.addInputSlot(0, 0).setStandardSlotBackground();
            inputSlot.addIngredients(cauldronRecipe.input());
            IRecipeSlotBuilder outputSlot = builder.addOutputSlot(90, 0).setOutputSlotBackground();
            outputSlot.addIngredients(cauldronRecipe.output());
        }
    }
}
