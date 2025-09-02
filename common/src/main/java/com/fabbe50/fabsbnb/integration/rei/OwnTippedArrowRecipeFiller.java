package com.fabbe50.fabsbnb.integration.rei;

import com.fabbe50.fabsbnb.data.OwnTippedArrowRecipe;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.*;

public class OwnTippedArrowRecipeFiller implements CraftingRecipeFiller<OwnTippedArrowRecipe> {
    @Override
    public Collection<Display> apply(RecipeHolder<OwnTippedArrowRecipe> recipe) {
        EntryIngredient arrowStack = EntryIngredient.of(EntryStacks.of(Items.ARROW));
        Set<ResourceLocation> registeredPotions = new HashSet<>();
        List<Display> displays = new ArrayList<>();

        RegistryAccess registryAccess = BasicDisplay.registryAccess();
        registryAccess.lookup(Registries.POTION).stream()
                .flatMap(Registry::listElements)
                .map(reference -> PotionContents.createItemStack(ModRegistries.OWN_LINGERING_POTION_ITEM.get(), reference))
                .forEach(itemStack -> {
                    PotionContents potion = itemStack.get(DataComponents.POTION_CONTENTS);
                    if (potion.potion().isPresent() && potion.potion().get().unwrapKey().isPresent() && registeredPotions.add(potion.potion().get().unwrapKey().get().location())) {
                        List<EntryIngredient> input = new ArrayList<>();
                        for (int i = 0; i < 4; i++)
                            input.add(arrowStack);
                        input.add(EntryIngredients.of(itemStack));
                        for (int i = 0; i < 4; i++)
                            input.add(arrowStack);
                        ItemStack outputStack = new ItemStack(ModRegistries.OWN_TIPPED_ARROW_ITEM.get(), 8);
                        outputStack.set(DataComponents.POTION_CONTENTS, potion);
                        displays.add(new DefaultCustomDisplay(input, List.of(EntryIngredients.of(outputStack)), Optional.of(recipe.id().location())));
                    }
                });

        return displays;
    }

    @Override
    public Class<OwnTippedArrowRecipe> getRecipeClass() {
        return OwnTippedArrowRecipe.class;
    }
}