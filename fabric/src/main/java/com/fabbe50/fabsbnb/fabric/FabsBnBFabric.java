package com.fabbe50.fabsbnb.fabric;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.fabric.integration.Trinkets;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.registries.PotionBrewingRecipes;
import com.fabbe50.fabsbnb.registries.TabList;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class FabsBnBFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        FabsBnB.init();

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((creativeModeTab, event) -> {
            if (creativeModeTab.equals(ModRegistries.TAB.get())) {
                TabData data = new TabData(event);
                TabList<FabricItemGroupEntries, TabData> tabList = new TabList<>();
                tabList.registerTab(data, event.getContext().holders());
            }
        });
        FabricBrewingRecipeRegistryBuilder.BUILD.register(PotionBrewingRecipes::register);

        initIntegrations();
    }

    private void initIntegrations() {
        if (Platform.isModLoaded("trinkets")) {
            FabsBnB.log("Trinkets loaded! Registering trinkets...");
            Trinkets.registerTrinkets();
        }
    }

    public static class TabData extends TabList.TabReg<FabricItemGroupEntries> {
        public TabData(FabricItemGroupEntries regHandler) {
            super(regHandler);
        }

        @Override
        public void accept(ItemStack stack) {
            getRegHandler().accept(stack);
        }
    }
}
