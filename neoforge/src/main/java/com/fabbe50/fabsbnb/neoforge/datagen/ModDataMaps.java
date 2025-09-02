package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDataMaps extends DataMapProvider {

    /**
     * Create a new provider.
     *
     * @param packOutput     the output location
     * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
     */
    protected ModDataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(getHolder(Items.ROTTEN_FLESH), new Compostable(0.5f), false)
                .add(getHolder(Items.BAMBOO), new Compostable(0.3f), false)
                .add(getHolder(Items.POISONOUS_POTATO), new Compostable(1.0f), false)
                .add(getHolder(Items.SPIDER_EYE), new Compostable(0.3f), false)
                .add(getHolder(Items.CHORUS_FRUIT), new Compostable(0.3f), false)
                .add(getHolder(Items.CHORUS_FLOWER), new Compostable(0.3f), false);
    }

    private Holder<Item> getHolder(Item item) {
        return ModRegistries.ITEMS.getHolder(ModRegistries.ITEMS.getId(item));
    }
}
