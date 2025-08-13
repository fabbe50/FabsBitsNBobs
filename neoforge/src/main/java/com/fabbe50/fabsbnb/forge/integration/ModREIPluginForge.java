package com.fabbe50.fabsbnb.forge.integration;

import com.fabbe50.fabsbnb.integration.ModREIPlugin;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.forge.REIPluginClient;

@REIPluginClient
public class ModREIPluginForge implements REIClientPlugin {
    private final ModREIPlugin REI_PLUGIN = new ModREIPlugin();

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
