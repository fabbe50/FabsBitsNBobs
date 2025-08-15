package com.fabbe50.fabsbnb.integration;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.data.CauldronConversionData;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<CauldronConversionDisplay> CAULDRON_CONVERSION_DISPLAY = CategoryIdentifier.of(FabsBnB.location("cauldron_conversion"));

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        Map<Item, Item> conversionMap = CauldronConversionData.getConversionMap();
        for (Item input : conversionMap.keySet()) {
            registry.add(new CauldronConversionDisplay(List.of(EntryIngredients.of(input)), List.of(EntryIngredients.of(conversionMap.get(input)))));
        }
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new CauldronConversionCategory());
    }

    @Override
    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        rule.hide(() -> List.of(EntryStacks.of(ModRegistries.FULL_WATER_CAULDRON.get())));
    }

    public static class CauldronConversionCategory implements DisplayCategory<CauldronConversionDisplay> {
        @Override
        public CategoryIdentifier<? extends CauldronConversionDisplay> getCategoryIdentifier() {
            return CAULDRON_CONVERSION_DISPLAY;
        }

        @Override
        public Component getTitle() {
            return FabsBnB.translatable("cauldron_conversion");
        }

        @Override
        public Renderer getIcon() {
            return EntryStacks.of(ModRegistries.FULL_WATER_CAULDRON.get());
        }

        @Override
        public List<Widget> setupDisplay(CauldronConversionDisplay display, Rectangle bounds) {
            Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
            List<Widget> widgets = new ArrayList<>();
            widgets.add(Widgets.createRecipeBase(bounds));
            widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
            widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                    .entries(display.getOutputEntries().getFirst())
                    .disableBackground()
                    .markOutput());
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y - 5))
                    .entries(display.getInputEntries().getFirst())
                    .markInput());
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 14))
                    .entries(List.of(EntryStacks.of(ModRegistries.FULL_WATER_CAULDRON.get())))
                    .markInput());

            return widgets;
        }
    }

    public static class CauldronConversionDisplay extends BasicDisplay {
        public CauldronConversionDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
            super(inputs, outputs);
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return CAULDRON_CONVERSION_DISPLAY;
        }
    }
}
