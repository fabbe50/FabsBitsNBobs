package com.fabbe50.fabsbnb.datagen;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BlockDrops extends VanillaBlockLoot {
    @Override
    protected void generate() {
        dropSelf(ModRegistries.LAVA_SPONGE.get());
        dropSelf(ModRegistries.LAVA_SPONGE_USED.get());
        dropSelf(ModRegistries.PUSHER_BLOCK.get());
        dropSelf(ModRegistries.THIN_LIGHT.get());
        dropSelf(ModRegistries.POWERED_THIN_LIGHT.get());
        dropSelf(ModRegistries.BLOCK_PLACER.get());
        dropSelf(ModRegistries.BLOCK_BREAKER.get());
        dropSelf(ModRegistries.BLOCK_DETECTOR.get());
        dropSelf(ModRegistries.XP_HOLDER.get());
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        this.generate();
        Set<ResourceLocation> set = new HashSet<>();

        for(Block block : this.getKnownBlocks()) {
            if (block.isEnabled(this.enabledFeatures)) {
                ResourceLocation resourcelocation = block.getLootTable();
                if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
                    LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
                    if (loottable$builder == null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loot-table '%s' for '%s'", resourcelocation, BuiltInRegistries.BLOCK.getKey(block)));
                    }
                    biConsumer.accept(resourcelocation, loottable$builder);
                }
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
        }
    }

    protected Iterable<Block> getKnownBlocks() {
        return ModRegistries.BLOCK_LIST.stream().map(Supplier::get).toList();
    }
}
