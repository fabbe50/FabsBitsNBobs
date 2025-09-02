package com.fabbe50.fabsbnb.neoforge.integration;

import com.fabbe50.fabsbnb.integration.rei.ModREIClientPlugin;
import com.fabbe50.fabsbnb.integration.rei.ModREICommonPlugin;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.forge.REIPluginCommon;

public class ModREIPluginForge implements REIClientPlugin {
    @REIPluginClient
    public static class Client implements REIClientPlugin {
        private final ModREIClientPlugin REI_PLUGIN = new ModREIClientPlugin();

        @Override
        public void registerDisplays(DisplayRegistry registry) {
            REI_PLUGIN.registerDisplays(registry);
        }

        @Override
        public void registerCategories(CategoryRegistry registry) {
            REI_PLUGIN.registerCategories(registry);
        }

        @Override
        public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
            REI_PLUGIN.registerBasicEntryFiltering(rule);
        }
    }

    @REIPluginCommon
    public static class Common implements REICommonPlugin {
        private final ModREICommonPlugin REI_PLUGIN = new ModREICommonPlugin();

        @Override
        public void registerItemComparators(ItemComparatorRegistry registry) {
            REI_PLUGIN.registerItemComparators(registry);
        }
    }
}
