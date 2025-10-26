package com.fabbe50.fabsbnb.neoforge.datagen.loot;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BlockDrops extends VanillaBlockLoot {
    public BlockDrops(HolderLookup.Provider provider) {
        super(provider);
    }

    @Override
    protected void generate() {
        dropSelf(ModRegistries.LAVA_SPONGE.get());
        dropSelf(ModRegistries.LAVA_SPONGE_USED.get());
        dropSelf(ModRegistries.SLOW_PUSHER_BLOCK.get());
        dropSelf(ModRegistries.NORMAL_PUSHER_BLOCK.get());
        dropSelf(ModRegistries.FAST_PUSHER_BLOCK.get());
        dropSelf(ModRegistries.THIN_LIGHT.get());
        dropSelf(ModRegistries.POWERED_THIN_LIGHT.get());
        dropSelf(ModRegistries.BLOCK_PLACER.get());
        dropSelf(ModRegistries.BLOCK_BREAKER.get());
        dropSelf(ModRegistries.BLOCK_DETECTOR.get());
        dropSelf(ModRegistries.XP_HOLDER.get());
        dropSelf(ModRegistries.SLIME_SAND.get());
        dropSelf(ModRegistries.STRUCTURAL_GOOP.get());
        dropWhenSilkTouch(ModRegistries.STRUCTURAL_GLASS.get());
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        this.generate();
        Set<ResourceKey<LootTable>> set = new HashSet<>();

        for(Block block : this.getKnownBlocks()) {
            if (block.isEnabled(this.enabledFeatures)) {
                ResourceKey<LootTable> lootTableKey = block.getLootTable().orElseThrow();
                if (set.add(lootTableKey)) {
                    LootTable.Builder loottable$builder = this.map.remove(lootTableKey);
                    if (loottable$builder == null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loot-table '%s' for '%s'", lootTableKey, BuiltInRegistries.BLOCK.getKey(block)));
                    }
                    biConsumer.accept(lootTableKey, loottable$builder);
                }
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
        }
    }

    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModRegistries.BLOCK_LIST.stream().map(Supplier::get).toList();
    }
}
