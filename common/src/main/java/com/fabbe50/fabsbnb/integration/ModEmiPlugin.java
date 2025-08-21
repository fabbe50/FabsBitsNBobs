package com.fabbe50.fabsbnb.integration;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.data.CauldronConversionData;
import com.fabbe50.fabsbnb.data.ItemInformations;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.item.enchantments.IEnchantment;
import com.fabbe50.fabsbnb.world.item.enchantments.VeinMinerEnchant;
import com.mojang.datafixers.util.Pair;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.*;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {
    public static final EmiStack CAULDRON = EmiStack.of(Items.CAULDRON);
    public static final EmiRecipeCategory CAULDRON_CONVERSION = new EmiRecipeCategory(FabsBnB.location("cauldron_conversion"), CAULDRON);

    @Override
    public void register(EmiRegistry emiRegistry) {
        FabsBnB.log("EMI is loaded! Registering plugin...");

        emiRegistry.addCategory(CAULDRON_CONVERSION);
        emiRegistry.addWorkstation(CAULDRON_CONVERSION, CAULDRON);

        Map<String, Pair<List<Item>, List<Component>>> itemComponentsMap = ItemInformations.getItemComponentsMap();
        for (String name : itemComponentsMap.keySet()) {
            emiRegistry.addRecipe(new EmiInfoRecipe(
                    itemComponentsMap.get(name).getFirst().stream().map(item -> EmiIngredient.of(Ingredient.of(item))).toList(),
                    itemComponentsMap.get(name).getSecond(),
                    getDynamicLocation(name)
            ));
        }

        Map<String, Pair<TagKey<Item>, List<Component>>> tagComponents = ItemInformations.getTagKeyComponents();
        for (String name : tagComponents.keySet()) {
            emiRegistry.addRecipe(new EmiInfoRecipe(
                    List.of(EmiIngredient.of(Ingredient.of(tagComponents.get(name).getFirst()))),
                    tagComponents.get(name).getSecond(),
                    getDynamicLocation(name)
            ));
        }

        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Map<IEnchantment, List<Component>> enchantmentComponents = ItemInformations.getEnchantmentComponents();
            for (IEnchantment enchantment : enchantmentComponents.keySet()) {
                List<EmiIngredient> stackList = new ArrayList<>();
                for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                    stackList.add(EmiIngredient.of(Ingredient.of(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Utilities.getHolder(level, enchantment.getResourceKey()), i)))));
                }
                if (enchantment instanceof VeinMinerEnchant veinMinerEnchant) {
                    stackList.add(EmiIngredient.of(veinMinerEnchant.getBlockFilter()));
                }
                emiRegistry.addRecipe(new EmiInfoRecipe(
                        stackList,
                        enchantmentComponents.get(enchantment),
                        getDynamicLocation(enchantment.getResourceKey().location().getPath())
                ));
            }
        }

        Map<String, Pair<List<ItemStack>, List<Component>>> stackComponents = ItemInformations.getItemStackComponents();
        for (String name : stackComponents.keySet()) {
            emiRegistry.addRecipe(new EmiInfoRecipe(
                    stackComponents.get(name).getFirst().stream().map(Ingredient::of).map(EmiIngredient::of).toList(),
                    stackComponents.get(name).getSecond(),
                    getDynamicLocation(name)
            ));
        }

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
            widgetHolder.addSlot(inputs.getFirst(), 0, 0);
            widgetHolder.addSlot(water, rl, 0);
            widgetHolder.addSlot(outputs.getFirst(), 90, 0).recipeContext(this);
        }
    }

    private static ResourceLocation getDynamicLocation(String name) {
        return FabsBnB.location("/" + name + "_description");
    }
}
