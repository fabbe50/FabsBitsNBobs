package com.fabbe50.fabsbnb.data;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class OwnTippedArrowRecipe extends CustomRecipe {
    public OwnTippedArrowRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingInput recipeInput, Level level) {
        int width = recipeInput.width();
        int height = recipeInput.height();

        if (width != 3 || height != 3 || recipeInput.ingredientCount() != 9) {
            return false;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ItemStack stack = recipeInput.getItem(x, y);
                if (stack.isEmpty()) {
                    return false;
                }

                if (x == 1 && y == 1) {
                    if (!stack.is(ModRegistries.OWN_LINGERING_POTION_ITEM.get())) {
                        return false;
                    }
                }
                else if (!stack.is(Items.ARROW)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput recipeInput, HolderLookup.Provider provider) {
        ItemStack itemStack = recipeInput.getItem(1, 1);
        if (!itemStack.is(ModRegistries.OWN_LINGERING_POTION_ITEM.get())) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemStack2 = new ItemStack(ModRegistries.OWN_TIPPED_ARROW_ITEM.get(), 8);
            itemStack2.set(DataComponents.POTION_CONTENTS, itemStack.get(DataComponents.POTION_CONTENTS));
            return itemStack2;
        }
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRegistries.OWN_TIPPED_ARROW_RECIPE_SERIALIZER.get();
    }
}
