package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTags extends EnchantmentTagsProvider {
    public ModEnchantmentTags(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(arg, completableFuture, FabsBnB.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptional(ModRegistries.ORE_MINER)
                .addOptional(ModRegistries.TREE_CHOPPER)
                .addOptional(ModRegistries.LEAF_BREAKER)
                .addOptional(ModRegistries.CAPTURING)
                .addOptional(ModRegistries.HARVESTING)
                .addOptional(ModRegistries.TILLING)
                .addOptional(ModRegistries.SCYTHE);
    }
}
