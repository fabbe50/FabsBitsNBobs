package com.fabbe50.fabsbnb.neoforge.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public record TreasureChestLoot(HolderLookup.Provider provider) implements LootTableSubProvider {
    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {

    }
}
