package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.neoforge.datagen.loot.ModLootTableProvider;
import com.fabbe50.fabsbnb.neoforge.datagen.registrysets.RegistrySets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = FabsBnB.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        event.createProvider(Translations::new);

        event.createBlockAndItemTags(ModBlockTags::new, (packOutput1, provider, completableFuture2) -> new ModItemTags(packOutput1, provider));
        event.createProvider(ModEnchantmentTags::new);
        event.createProvider(Recipes.Runner::new);
        event.createProvider(ModLootTableProvider::new);
        event.createProvider(RegistrySets::new);
        event.createProvider(ModDataMaps::new);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Server event) {

    }
}
