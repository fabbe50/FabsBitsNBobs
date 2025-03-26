package com.fabbe50.fabsbnb.integration;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.data.CauldronConversionData;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    public static final EmiStack CAULDRON = EmiStack.of(Items.CAULDRON);
    public static final EmiRecipeCategory CAULDRON_CONVERSION = new EmiRecipeCategory(FabsBnB.location("cauldron_conversion"), CAULDRON);

    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(CAULDRON_CONVERSION);
        emiRegistry.addWorkstation(CAULDRON_CONVERSION, CAULDRON);

        Map<Item, Item> cauldronConversionMap = CauldronConversionData.getConversionMap();
        for (Item ingredient : cauldronConversionMap.keySet()) {
            emiRegistry.addRecipe(new CauldronRecipe(EmiIngredient.of(Ingredient.of(ingredient)), EmiStack.of(cauldronConversionMap.get(ingredient))));
        }
    }

    public static class CauldronRecipe extends BasicEmiRecipe {
        EmiIngredient water = EmiIngredient.of(Ingredient.of(ModRegistries.FULL_WATER_CAULDRON.get()));

        public CauldronRecipe(EmiIngredient ingredient, EmiStack result) {
            super(CAULDRON_CONVERSION, FabsBnB.location("/" + result.getId().getPath()), 108, 18);
            this.inputs.add(ingredient);
            this.outputs.add(result);
        }

        @Override
        public void addWidgets(WidgetHolder widgetHolder) {
            int lr = 18;
            int ol = this.width - 18;
            int rl = (lr + ol) / 2 - 9 - 4;
            int rr = rl + 18;
            widgetHolder.addTexture(EmiTexture.PLUS, (lr + rl) / 2 - EmiTexture.PLUS.width / 2, 3);
            widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, (rr + ol) / 2 - EmiTexture.EMPTY_ARROW.width / 2, 1);
            widgetHolder.addSlot(inputs.get(0), 0, 0);
            widgetHolder.addSlot(water, rl, 0);
            widgetHolder.addSlot(outputs.get(0), 90, 0).recipeContext(this);
        }
    }
}
