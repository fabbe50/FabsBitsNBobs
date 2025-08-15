package com.fabbe50.fabsbnb.fabric;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.fabric.integration.Trinkets;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.registries.PotionBrewingRecipes;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ModInitializer;
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

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((creativeModeTab, fabricItemGroupEntries) -> {
            if (creativeModeTab.equals(ModRegistries.TAB.get())) {
                FabsBnB.log("Setting up creative tab...");
                for (RegistrySupplier<Item> item : ModRegistries.ITEM_LIST) {
                    ItemStack stack = new ItemStack(item.get());
                    if (!stack.isEmpty()) {
                        fabricItemGroupEntries.accept(stack);
                    }
                }
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
}
