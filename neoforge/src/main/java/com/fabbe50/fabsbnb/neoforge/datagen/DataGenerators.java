package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.neoforge.datagen.loot.BlockDrops;
import com.fabbe50.fabsbnb.neoforge.datagen.registrysets.RegistrySets;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = FabsBnB.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = CompletableFuture.supplyAsync(RegistrySets::createLookup, Util.backgroundExecutor());

        generator.addProvider(event.includeServer(), new Recipes(packOutput, lookupProvider));
        ModBlockTags blockTags = new ModBlockTags(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ModItemTags(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModEnchantmentTags(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(
                        new LootTableProvider.SubProviderEntry(BlockDrops::new, LootContextParamSets.BLOCK)
                ), lookupProvider));
        generator.addProvider(event.includeServer(), new RegistrySets(packOutput, lookupProvider));
    }
}
